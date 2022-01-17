package com.codingame.game;

import static com.codingame.game.Util.otherId;

import com.codingame.game.actions.AbstractAction;
import com.codingame.game.actions.BarkAction;
import com.codingame.game.actions.MoveAction;
import com.codingame.game.actions.ShearAction;
import com.codingame.game.actions.TransferWoolAction;
import com.codingame.game.entity.Dog;
import com.codingame.game.entity.Entity;
import com.codingame.game.entity.Sheep;
import com.codingame.game.entity.Shepherd;
import com.codingame.gameengine.module.entities.GraphicEntityModule;
import com.codingame.gameengine.module.entities.Group;
import com.google.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.function.Function;
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


    public void drawInit(int i, int i1, int bigCellSize, int i2, int i3) {
        board.drawInit(i, i1, bigCellSize, i2);
        entitiesGroup = graphicEntityModule.createGroup().setZIndex(1);
        sheeps.forEach(s -> entitiesGroup.add(s.drawInit(graphicEntityModule.createCircle(), i, i1, bigCellSize,constsSettings.mapSize)));
        dogs.forEach(e -> entitiesGroup.add(e.drawInit(graphicEntityModule.createCircle(), i, i1, bigCellSize,constsSettings.mapSize)));
        shepherds.forEach(e -> entitiesGroup.add(e.drawInit(graphicEntityModule.createCircle(), i, i1, bigCellSize,constsSettings.mapSize)));
        sheeps.forEach(Entity::draw);
        dogs.forEach(Entity::draw);
        shepherds.forEach(Entity::draw);
    }

    public int init(Random random) {
        this.random = random;
        constsSettings = new ConstsSettings();
        board.init(constsSettings.mapSize);
        sheeps.add(new Sheep(new Vector2(3.5, 3.5)));
        sheeps.add(new Sheep(new Vector2(4, 4)));
        sheeps.add(new Sheep(new Vector2(5, 5)));
        shepherds.add(new Shepherd(new Vector2(1, 1), 0));
        shepherds.add(new Shepherd(new Vector2(8, 8), 1));
        dogs.add(new Dog(new Vector2(2, 2), 0));
        dogs.add(new Dog(new Vector2(7, 7), 1));
        return constsSettings.turns;
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

    public List<String> getSheepsInput() {
        List<String> res = new ArrayList<>();
        res.add(Integer.toString(sheeps.size()));
        sheeps.forEach(s -> res.add(s.toString()));
        return res;
    }

    public List<String> getDogsInput(Player p) {
        List<String> res = new ArrayList<>();
        Map<Integer,List<Dog>> split = dogs.stream().collect(Collectors.groupingBy(Entity::getOwner));
        res.add(Integer.toString(split.get(p.getIndex()).size()));
        split.get(p.getIndex()).forEach(d -> res.add(d.toString()));
        res.add(Integer.toString(split.get(otherId(p.getIndex())).size()));
        split.get(otherId(p.getIndex())).forEach(d -> res.add(d.toString()));
        return res;
    }

    public List<String> getShedsInput(){
        return board.getShedsInput();
    }

    public List<String> getShepherdsInput(Player p){
        List<String> res = new ArrayList<>();
        Map<Integer,List<Shepherd>> split = shepherds.stream().collect(Collectors.groupingBy(Entity::getOwner));
        res.add(Integer.toString(split.get(p.getIndex()).size()));
        split.get(p.getIndex()).forEach(s -> res.add(s.toStringFull()));
        res.add(Integer.toString(split.get(otherId(p.getIndex())).size()));
        split.get(otherId(p.getIndex())).forEach(s -> res.add(s.toString()));
        return res;
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
        List<Vector2> sheepsDirections = new ArrayList<>();
        boolean[] inDanger = new boolean[sheeps.size()];
        for (int i = 0; i < sheeps.size(); i++) {
            Sheep sheep = sheeps.get(i);
            if (sheep.isSheared) {
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
                if (!sheeps.get(j).isSheared && sheeps.get(j).getPosition()
                    .inRadius(sheep.getPosition(), constsSettings.entityRadius * 2.0)) {
                    sheepsDirections.add(sheepsDirections.get(j));
                    added = true;
                    break;
                }
            }
            if (!added) {
                sheepsDirections.add(randomMove(sheep.getPosition()));
            }
        }
        for (int i = 0; i < sheeps.size(); i++) {
            if (sheepsDirections.get(i) != null) {
                Sheep sheep = sheeps.get(i);
                sheep.move(sheepsDirections.get(i).addRand(random),
                    sheep.isScared ? constsSettings.sheepSpeed3 : (inDanger[i] ? constsSettings.sheepSpeed2 : constsSettings.sheepSpeed1));
            }
        }
    }

    private Vector2 randomMove(Vector2 position) {
        Vector2 randomV = new Vector2(random.nextDouble() * 2.0 - 1.0, random.nextDouble() * 2.0 - 1.0);
        if (position.getY() <= 1.0 && randomV.getY() < 0 || position.getY() >= constsSettings.mapSize && randomV.getY() > 0) {
            randomV.negateY();
        }
        if (position.getX() <= 1.0 && randomV.getX() < 0 || position.getX() >= constsSettings.mapSize && randomV.getX() > 0) {
            randomV.negateX();
        }
        return randomV;
    }

    private void bark(BarkAction action) {
        Dog dog = dogs.get(action.id);
        if (dog.getOwner() == action.player.getIndex() && dog.barkCoolDown <= 0) {
            sheeps.forEach(s -> s.onBark(dog.getPosition(), constsSettings.barkRadius));
            dog.barkCoolDown = constsSettings.barkCoolDown;
        }
    }

    private void move(MoveAction action) {
        if (action.isDog) {
            Dog dog = dogs.get(action.id);
            if (dog.getOwner() == action.player.getIndex()) {
                dog.move(action.direction, constsSettings.dogSpeed);
            }
        } else {
            Shepherd shepherd = shepherds.get(action.id);
            if (shepherd.getOwner() == action.player.getIndex()) {
                shepherd.move(action.direction, constsSettings.shepardSpeed);
            }
        }
    }

    private void shear(ShearAction action) {
        Shepherd shepherd = shepherds.get(action.id);
        if (shepherd.getOwner() != action.player.getIndex()) {
            return;
        }
        Sheep sheep = sheeps.get(action.sheepId);
        if (action.isStart) {
            if (!sheep.isSheared && sheep.getPosition().inRadius(shepherd.getPosition(), constsSettings.entityRadius * 2.0)) {
                sheep.isSheared = true;
                shepherd.shearing = action.sheepId;
            }
        } else {
            if (sheep.isSheared && shepherd.shearing == action.sheepId) {
                shepherd.shearing = -1;
                sheep.isSheared = false;
            }
        }
    }

    private void transfer(TransferWoolAction action) {
        Shepherd shepherd = shepherds.get(action.id);
        if (shepherd.getOwner() != action.player.getIndex()) {
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
}
