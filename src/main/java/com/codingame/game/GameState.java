package com.codingame.game;

import com.codingame.game.actions.AbstractAction;
import com.codingame.game.actions.BarkAction;
import com.codingame.game.actions.MoveAction;
import com.codingame.game.actions.ShearAction;
import com.codingame.game.actions.TransferWoolAction;
import com.codingame.game.board.Board;
import com.codingame.game.entity.Dog;
import com.codingame.game.entity.Entity;
import com.codingame.game.board.Shed;
import com.codingame.game.entity.Sheep;
import com.codingame.game.entity.Shepherd;
import com.codingame.game.util.Vector2;
import com.codingame.gameengine.module.entities.GraphicEntityModule;
import com.codingame.gameengine.module.entities.Group;
import com.codingame.gameengine.module.tooltip.TooltipModule;
import com.google.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;


/**
 * Main class containing game state and game logic
 */
public class GameState {

    @Inject
    private Board board;

    @Inject
    private GraphicEntityModule graphicEntityModule;
    private final List<Sheep> sheeps = new ArrayList<>();
    private final List<Dog> dogs = new ArrayList<>();
    private final List<Shepherd> shepherds = new ArrayList<>();

    private Group entitiesGroup;
    private Random random;
    private ConstsSettings constsSettings;

    private final List<Integer> turnSheared = new ArrayList<>();

    private int idGenSeq;


    /**
     * Initializes all graphical entities
     * @param orgX x pixel offset
     * @param orgY y pixel offset
     * @param cellSize size off one cell in pixels
     */
    public void drawInit(int orgX, int orgY, int cellSize) {
        board.drawInit(orgX, orgY, cellSize);
        entitiesGroup = graphicEntityModule.createGroup().setZIndex(1);
        sheeps.forEach(s -> entitiesGroup.add(
            s.drawInit(graphicEntityModule.createSprite(), orgX, orgY, cellSize, constsSettings.mapSizeX, constsSettings.mapSizeY)));
        dogs.forEach(e -> entitiesGroup.add(
            e.drawInit(graphicEntityModule.createSprite(), orgX, orgY, cellSize, constsSettings.mapSizeX, constsSettings.mapSizeY)));
        shepherds.forEach(
            e -> entitiesGroup.add(
                e.drawInit(graphicEntityModule.createSprite(), orgX, orgY, cellSize, constsSettings.mapSizeX, constsSettings.mapSizeY)));
        sheeps.forEach(Entity::draw);
        dogs.forEach(Entity::draw);
        shepherds.forEach(Entity::draw);
    }

    /**
     * Generates initial world state with units.
     * @param random random number generator
     * @return number of game turns.
     */
    public int init(Random random) {
        idGenSeq = 1;
        this.random = random;
        constsSettings = new ConstsSettings(random);
        board.init(constsSettings.mapSizeX, constsSettings.mapSizeY, random);
        generateSheep();
        int shepardCnt = random.nextInt(3) + 1;
        int dogCnt = random.nextInt(3) + 1;
        while (shepardCnt-- > 0) {
            double x = random.nextDouble() * 9;
            shepherds.add(new Shepherd(++idGenSeq, new Vector2(x, 1), 0));
            shepherds.add(new Shepherd(++idGenSeq, new Vector2(x, 13), 1));
        }
        while (dogCnt-- > 0) {
            double x = random.nextDouble() * 9;
            dogs.add(new Dog(++idGenSeq, new Vector2(x, 2), 0));
            dogs.add(new Dog(++idGenSeq, new Vector2(x, 12), 1));
        }
        return constsSettings.turns;
    }

    /**
     *
     * @return amount of single player's units
     */
    public int playerPawnsAmount() {
        return (shepherds.size() + dogs.size()) / 2;

    }

    private void generateSheep() {
        int sheepNo = random.nextInt(constsSettings.maxSheep - constsSettings.minSheep) + constsSettings.minSheep;
        while (sheepNo-- > 0) {
            sheeps.add(
                new Sheep(++idGenSeq, new Vector2(random.nextDouble() * constsSettings.mapSizeX, random.nextDouble() * constsSettings.mapSizeY),
                    constsSettings.initialSheepWool));
        }
    }

    /**
     * Updates state of all graphical entities.
     */
    public void draw() {
        sheeps.forEach(Sheep::draw);
        dogs.forEach(Entity::draw);
        shepherds.forEach(Entity::draw);
    }

    /**
     * @return constants for game as player's input.
     */
    public String getConstsString() {
        return constsSettings.toString();
    }

    /**
     * @return input line for player with all units counts.
     */
    public String getCounts() {
        List<String> res = new ArrayList<>();
        res.add(Integer.toString(sheeps.size()));
        res.add(Integer.toString(shepherds.size()));
        res.add(Integer.toString(dogs.size()));
        res.add(Integer.toString(board.getShedSize()));
        res.add(Integer.toString((shepherds.size() + dogs.size()) / 2));
        return String.join(" ", res);
    }

    /**
     * @return list of sheep information for player's input
     */
    public List<String> getSheepsInput() {
        List<String> res = new ArrayList<>();
        sheeps.forEach(s -> res.add(s.toString()));
        return res;
    }

    /**
     * @return list of dogs information for player's input
     */
    public List<String> getDogsInput() {
        return dogs.stream().map(Dog::toString).collect(Collectors.toList());
    }

    /**
     * @return list of shepherds information for player's input
     */
    public List<String> getShedsInput() {
        return board.getShedsInput();
    }

    /**
     * @return list of sheds information for player's input
     */
    public List<String> getShepherdsInput(Player p) {
        return shepherds.stream().map(Shepherd::toStringFull).collect(Collectors.toList());
    }

    /**
     * Resolves all players' actions, starting with MOVE actions.
     * @param actions list of players' actions
     */
    public void resolveActions(List<AbstractAction> actions) {
        dogs.forEach(d->d.used=false);
        shepherds.forEach(s->s.used=false);
        turnSheared.clear();
        actions.sort((a1,a2) -> {
            if(a1 instanceof MoveAction){
                if(a2 instanceof MoveAction){
                    return 0;
                }
                return -1;
            }
            return 1;
        });
        actions.forEach(a -> {
            if (a instanceof BarkAction) {
                bark((BarkAction) a);
            } else if (a instanceof MoveAction) {
                move((MoveAction) a);
            } else if (a instanceof ShearAction) {
                shear((ShearAction) a);
            } else if (a instanceof TransferWoolAction) {
                transfer((TransferWoolAction) a);
            }
        });
    }

    /**
     * Simulates sheep's movement and updates sheds ownership.
     */
    public void onTurnEnd() {
        dogs.forEach(Dog::onTurnEnd);
        board.updateSheds(dogs);
        List<Vector2> sheepsDirections = new ArrayList<>();
        boolean[] inDanger = new boolean[sheeps.size()];
        for (int i = 0; i < sheeps.size(); i++) {
            Sheep sheep = sheeps.get(i);
            if (sheep.shearedBy !=0 ) {
                sheepsDirections.add(null);
                continue;
            }
            Entity danger = dogs.stream().filter(dog -> dog.getPosition().inRadius(sheep.getPosition(), constsSettings.entityRadius * 2.0))
                .findFirst().orElse(null);
            if (danger == null) {
                danger = shepherds.stream().filter(s -> s.getPosition().inRadius(sheep.getPosition(), constsSettings.entityRadius * 2.0))
                    .findFirst().orElse(null);
            }
            if (danger != null) {
                sheepsDirections.add(sheep.getPosition().away(danger.getPosition()));
                inDanger[i] = true;
                continue;
            }
            boolean added = false;
            for (int j = 0; j < i; j++) {
                if (sheeps.get(j).shearedBy == 0 && sheeps.get(j).getPosition()
                    .inRadius(sheep.getPosition(), constsSettings.entityRadius * 2.0)) {
                    sheepsDirections.add(sheepsDirections.get(j));
                    added = true;
                    break;
                }
            }
            if (!added) {
                sheepsDirections.add(randomMove(sheep.getPosition(), sheep.lastMove));
            }
        }
        for (int i = 0; i < sheeps.size(); i++) {
            if (sheepsDirections.get(i) != null) {
                Sheep sheep = sheeps.get(i);
                Vector2 movement = sheepsDirections.get(i).addRand(random);
                sheep.move(movement,
                    sheep.isScared ? constsSettings.sheepSpeed3 : (inDanger[i] ? constsSettings.sheepSpeed2 : constsSettings.sheepSpeed1));
            }
        }
        for (Shepherd s : shepherds) {
            if (s.shearing == 0) {
                continue;
            }
            Sheep sh = sheeps.get(s.shearing - 1);
            if (sh.wool > 0 && s.wool < constsSettings.shepardMaxWool) {
                sh.decrWool();
                s.wool++;
            }
        }

    }

    private Vector2 randomMove(Vector2 position, Vector2 lastMove) {
        if (random.nextInt(100) < constsSettings.sameDirectionProbability) {
            return lastMove;
        }
        Vector2 randomV = new Vector2(random.nextDouble() * 2.0 - 1.0, random.nextDouble() * 2.0 - 1.0);
        if (position.getY() <= 1.0 && randomV.getY() < 0 || position.getY() >= constsSettings.mapSizeY && randomV.getY() > 0) {
            randomV.negateY();
        }
        if (position.getX() <= 1.0 && randomV.getX() < 0 || position.getX() >= constsSettings.mapSizeX && randomV.getX() > 0) {
            randomV.negateX();
        }
        return randomV;
    }

    private void bark(BarkAction action) {
        Dog dog = dogs.stream().filter(d -> d.id == action.id).findFirst().orElse(null);
        if (dog != null && !dog.used && dog.getOwner() == action.player.getIndex() && dog.barkCoolDown <= 0) {
            dog.used=true;
            sheeps.forEach(s -> s.onBark(dog.getPosition(), constsSettings.barkRadius));
            dog.barkCoolDown = constsSettings.barkCoolDown;
        }
    }

    private void move(MoveAction action) {
        Dog dog = dogs.stream().filter(d -> d.id == action.id).findFirst().orElse(null);
        if (dog != null && !dog.used &dog.getOwner() == action.player.getIndex()) {
            dog.used=true;
            dog.move(action.direction, constsSettings.dogSpeed);
        }
        Shepherd shepherd = shepherds.stream().filter(s -> s.id == action.id).findFirst().orElse(null);
        if (shepherd != null && !shepherd.used&& shepherd.getOwner() == action.player.getIndex()) {
            if(shepherd.shearing!=0){
                sheeps.get(shepherd.shearing-1).shearedBy = 0;
                shepherd.shearing=0;
            }
            shepherd.used=true;
            shepherd.move(action.direction, constsSettings.shepardSpeed);
        }
    }

    private void shear(ShearAction action) {
        if(turnSheared.contains(action.sheepId)){
            cancelShear(action.sheepId);
            return;
        }
        Shepherd shepherd = shepherds.stream().filter(s -> s.id == action.id).findFirst().orElse(null);
        if (shepherd == null || shepherd.used ||shepherd.getOwner() != action.player.getIndex()) {
            return;
        }
        shepherd.used=true;
        Sheep sheep = sheeps.get(action.sheepId - 1);
        if (sheep.shearedBy == 0 && sheep.getPosition().inRadius(shepherd.getPosition(), constsSettings.entityRadius * 2.0)) {
            sheep.shearedBy = shepherd.id;
            shepherd.shearing = action.sheepId;
        }

    }

    private void cancelShear(int sheepId){
        Sheep sheep =sheeps.stream().filter(s-> s.id==sheepId).findAny().orElse(null);
        if(sheep!= null && sheep.shearedBy!=0){
            Shepherd sp = shepherds.stream().filter(shepherd -> shepherd.id == sheep.shearedBy).findAny().get();
            sp.shearing=0;
            sheep.shearedBy=0;

        }
    }

    private void transfer(TransferWoolAction action) {
        Shepherd shepherd = shepherds.stream().filter(s -> s.id == action.id).findFirst().orElse(null);
        if (shepherd == null || shepherd.used ||shepherd.getOwner() != action.player.getIndex()) {
            return;
        }
        shepherd.used=true;
        if(shepherd.shearing!=0){
            sheeps.get(shepherd.shearing-1).shearedBy = 0;
            shepherd.shearing=0;
        }
        Shed shed = board.getShed(shepherd.getPosition());
        if (shed == null) {
            return;
        }
        if (action.isDeposit) {
            int amount = Math.min(action.amount, shepherd.wool);
            shed.wool += amount;
            shepherd.wool -= amount;
        } else {
            int amount = Math.min(Math.min(action.amount, shed.wool), constsSettings.shepardMaxWool - shepherd.wool);
            shed.wool -= amount;
            shepherd.wool += amount;
        }
    }

    /**
     * @param id player id
     * @return score for player with given id
     */
    public int getScore(int id) {
        return board.getScore(id);
    }

    /**
     * Updates tooltips.
     * @param tooltips Tooltip object for update
     */
    public void updateTooltip(TooltipModule tooltips) {
        //sheeps.forEach(s -> tooltips.setTooltipText(s.getSprite(), s.tooltipTxt()));
        dogs.forEach(d -> tooltips.setTooltipText(d.getSprite(), d.tooltipTxt()));
        shepherds.forEach(s -> tooltips.setTooltipText(s.getSprite(), s.tooltipTxt()));
        board.updateTooltip(tooltips);
    }
}
