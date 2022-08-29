package com.codingame.game;

import com.codingame.game.actions.AbstractAction;
import com.codingame.game.actions.BarkAction;
import com.codingame.game.actions.MoveAction;
import com.codingame.game.actions.ShearAction;
import com.codingame.game.actions.TransferWoolAction;
import com.codingame.game.entity.Dog;
import com.codingame.game.entity.Entity;
import com.codingame.game.entity.Shed;
import com.codingame.game.entity.Sheep;
import com.codingame.game.entity.Shepherd;
import com.codingame.gameengine.module.entities.GraphicEntityModule;
import com.codingame.gameengine.module.entities.Group;
import com.codingame.gameengine.module.tooltip.TooltipModule;
import com.google.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class GameState {

    @Inject
    private Board board;

    @Inject
    private GraphicEntityModule graphicEntityModule;
    private List<Sheep> sheeps = new ArrayList<>();
    private List<Dog> dogs = new ArrayList<>();
    private List<Shepherd> shepherds = new ArrayList<>();

    private Group entitiesGroup;
    private Random random;
    private ConstsSettings constsSettings;

    private int id;


    public void drawInit(int i, int i1, int bigCellSize, int i2, int i3) {
        board.drawInit(i, i1, bigCellSize, i2);
        entitiesGroup = graphicEntityModule.createGroup().setZIndex(1);
        sheeps.forEach(s -> entitiesGroup.add(
            s.drawInit(graphicEntityModule.createSprite(), i, i1, bigCellSize, constsSettings.mapSizeX, constsSettings.mapSizeY)));
        dogs.forEach(e -> entitiesGroup.add(
            e.drawInit(graphicEntityModule.createSprite(), i, i1, bigCellSize, constsSettings.mapSizeX, constsSettings.mapSizeY)));
        shepherds.forEach(
            e -> entitiesGroup.add(
                e.drawInit(graphicEntityModule.createSprite(), i, i1, bigCellSize, constsSettings.mapSizeX, constsSettings.mapSizeY)));
        sheeps.forEach(Entity::draw);
        dogs.forEach(Entity::draw);
        shepherds.forEach(Entity::draw);
    }

    public int init(Random random) {
        id = 1;
        this.random = random;
        constsSettings = new ConstsSettings(random);
        board.init(constsSettings.mapSizeX, constsSettings.mapSizeY, random);
        generateSheep();
        int shepardCnt = random.nextInt(3) + 1;
        int dogCnt = random.nextInt(3) + 1;
        while (shepardCnt-- > 0) {
            double x = random.nextDouble() * 9;
            shepherds.add(new Shepherd(++id, new Vector2(x, 1), 0));
            shepherds.add(new Shepherd(++id, new Vector2(x, 13), 1));
        }
        while (dogCnt-- > 0) {
            double x = random.nextDouble() * 9;
            dogs.add(new Dog(++id, new Vector2(x, 2), 0));
            dogs.add(new Dog(++id, new Vector2(x, 12), 1));
        }
        return constsSettings.turns;
    }

    public int pawnsNo() {
        return (shepherds.size() + dogs.size()) / 2;

    }

    private void generateSheep() {
        int sheepNo = random.nextInt(constsSettings.maxSheep - constsSettings.minSheep) + constsSettings.minSheep;
        while (sheepNo-- > 0) {
            sheeps.add(
                new Sheep(++id, new Vector2(random.nextDouble() * constsSettings.mapSizeX, random.nextDouble() * constsSettings.mapSizeY),
                    constsSettings.initialSheepWool));
        }
    }

    public void draw() {
        board.draw();
        sheeps.forEach(Sheep::draw);
        dogs.forEach(Entity::draw);
        shepherds.forEach(Entity::draw);
    }

    public String getConsts() {
        return constsSettings.toString();
    }

    public String getCounts() {
        List<String> res = new ArrayList<>();
        res.add(Integer.toString(sheeps.size()));
        res.add(Integer.toString(shepherds.size()));
        res.add(Integer.toString(dogs.size()));
        res.add(Integer.toString(board.getShedSize()));
        res.add(Integer.toString((shepherds.size() + dogs.size()) / 2));
        return String.join(" ", res);
    }

    public List<String> getSheepsInput() {
        List<String> res = new ArrayList<>();
        //res.add(Integer.toString(sheeps.size()));
        sheeps.forEach(s -> res.add(s.toString()));
        return res;
    }

    public List<String> getDogsInput() {
        return dogs.stream().map(Dog::toString).collect(Collectors.toList());
    }

    public List<String> getShedsInput() {
        return board.getShedsInput();
    }

    public List<String> getShepherdsInput(Player p) {
        return shepherds.stream().map(Shepherd::toStringFull).collect(Collectors.toList());
    }

    public void resolveActions(List<AbstractAction> actions) {
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
        if (dog != null && dog.getOwner() == action.player.getIndex() && dog.barkCoolDown <= 0) {
            sheeps.forEach(s -> s.onBark(dog.getPosition(), constsSettings.barkRadius));
            dog.barkCoolDown = constsSettings.barkCoolDown;
        }
    }

    private void move(MoveAction action) {
        Dog dog = dogs.stream().filter(d -> d.id == action.id).findFirst().orElse(null);
        if (dog != null && dog.getOwner() == action.player.getIndex()) {
            dog.move(action.direction, constsSettings.dogSpeed);
        }
        Shepherd shepherd = shepherds.stream().filter(s -> s.id == action.id).findFirst().orElse(null);
        if (shepherd != null && shepherd.getOwner() == action.player.getIndex()) {
            if(shepherd.shearing!=0){
                sheeps.get(shepherd.shearing-1).shearedBy = 0;
                shepherd.shearing=0;
            }
            shepherd.move(action.direction, constsSettings.shepardSpeed);
        }
    }

    private void shear(ShearAction action) {
        Shepherd shepherd = shepherds.stream().filter(s -> s.id == action.id).findFirst().orElse(null);
        if (shepherd == null || shepherd.getOwner() != action.player.getIndex()) {
            return;
        }
        Sheep sheep = sheeps.get(action.sheepId - 1);
        if (sheep.shearedBy == 0 && sheep.getPosition().inRadius(shepherd.getPosition(), constsSettings.entityRadius * 2.0)) {
            sheep.shearedBy = shepherd.id;
            shepherd.shearing = action.sheepId;
        }

    }

    private void transfer(TransferWoolAction action) {
        Shepherd shepherd = shepherds.stream().filter(s -> s.id == action.id).findFirst().orElse(null);
        if (shepherd == null || shepherd.getOwner() != action.player.getIndex()) {
            return;
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

    public int getScore(int id) {
        return board.getScore(id);
    }

    public void updateTooltip(TooltipModule tooltips) {
        //sheeps.forEach(s -> tooltips.setTooltipText(s.getSprite(), s.tooltipTxt()));
        dogs.forEach(d -> tooltips.setTooltipText(d.getSprite(), d.tooltipTxt()));
        shepherds.forEach(s -> tooltips.setTooltipText(s.getSprite(), s.tooltipTxt()));
        board.updateTooltip(tooltips);
    }
}
