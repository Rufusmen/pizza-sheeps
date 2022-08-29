import static java.lang.Math.sqrt;


import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;

public class Player2 {

    static float entityRadius;
    static int myId;

    static class Actor {
        int id;
        float x, y;
        int owner;

        public Actor(int id,float x, float y, int owner) {
            this.id=id;
            this.owner = owner;
            this.x = x;
            this.y = y;
        }
    }

    static class Shepherd extends Player1.Actor {

        int wool, shearing;

        public Shepherd(int id,float x, float y, int wool, int shearing, int owner) {
            super(id,x, y, owner);
            this.wool = wool;
            this.shearing = shearing;
        }
    }

    static class Sheep extends Player1.Actor {

        int wool;
        int isSheared;

        public Sheep(int id,float x, float y, int wool, int isSheared) {
            super(id,x, y, -1);
            this.wool = wool;
            this.isSheared = isSheared;
        }
    }

    static class Shed {

        int x;
        int y;
        int owner;
        int wool;


        public Shed(int x, int y, int owner, int wool) {
            this.x = x;
            this.y = y;
            this.owner = owner;
            this.wool = wool;

        }

    }

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        Random random = new Random(0);
        myId = in.nextInt();
        int initialSheepWool = in.nextInt();
        int shepardMaxWool = in.nextInt();
        entityRadius = in.nextFloat();
        float sheepSpeed1 = in.nextFloat();
        float sheepSpeed2 = in.nextFloat();
        float sheepSpeed3 = in.nextFloat();
        float shepardSpeed = in.nextFloat();
        float dogSpeed = in.nextFloat();
        float dangerRadius = in.nextFloat();
        int barkCoolDown = in.nextInt();
        float barkRadius = in.nextFloat();
        int calmCoolDown = in.nextInt();

        int sheepCnt = in.nextInt();
        int shepherdsCnt = in.nextInt();
        int dogsCnt = in.nextInt();
        int shedsCnt = in.nextInt();
        Set<Integer> dogStay = new HashSet<>();
        int allUnits = in.nextInt();
        Player1.Shed sed = null;
        while (true) {
            //int sheepCnt = in.nextInt();
            List<Player1.Sheep> sheep = new ArrayList<>();
            for (int i = 0; i < sheepCnt; i++) {
                sheep.add(new Player1.Sheep(in.nextInt(),in.nextFloat(), in.nextFloat(), in.nextInt(), in.nextInt()));
            }
            //int myShepherdsCnt = in.nextInt();
            List<Player1.Shepherd> shepherds = new ArrayList<>();
            for (int i = 0; i < shepherdsCnt; i++) {
                shepherds.add(new Player1.Shepherd(in.nextInt(),in.nextFloat(), in.nextFloat(), in.nextInt(), in.nextInt(), in.nextInt()));
            }
            //int enemyShepherdsCnt = in.nextInt();
            //int enemyDogsCnt = in.nextInt();
            List<Player1.Actor> dogs = new ArrayList<>();
            for (int i = 0; i < dogsCnt; i++) {
                dogs.add(new Player1.Actor(in.nextInt(),in.nextFloat(), in.nextFloat(), in.nextInt()));
            }
            //int shedsCnt = in.nextInt();
            List<Player1.Shed> sheds = new ArrayList<>();
            for (int i = 0; i < shedsCnt; i++) {

                sheds.add(new Player1.Shed(in.nextInt(), in.nextInt(), in.nextInt(), in.nextInt()));
            }
            for (int i = 0; i < shepherdsCnt; i++) {
                Player1.Shepherd s = shepherds.get(i);
                if (s.owner == myId) {
                    playShepard(s, i, sheds, sheep);
                }
            }
            if (sed == null) {
                sed = sheds.stream().filter(s -> s.owner == -1).findFirst().orElse(null);
            }
            for (int i = 0; i < dogsCnt; i++) {
                Player1.Actor d = dogs.get(i);
                if (d.owner == myId) {
                    if (sed == null || dogStay.contains(i)) {
                        System.out.printf("MOVE %d 0 0%n", d.id);
                    } else {
                        if (((int) d.x) == sed.x && ((int) d.y) == sed.y) {
                            dogStay.add(i);
                            sed = null;
                            System.out.printf("MOVE %d 0 0%n", d.id);
                        } else {
                            System.out.printf(Locale.ROOT, "MOVE %d %f %f%n", d.id, sed.x+0.5 - d.x, sed.y+0.5 - d.y);
                        }
                    }
                }
            }

        }
    }

    static void playShepard(Player1.Shepherd sp, int id, List<Player1.Shed> sheds, List<Player1.Sheep> sheep) {
        if (sp.shearing != 0) {
            Player1.Sheep s = sheep.get(sp.shearing - 1);
            if (s.wool <= 0) {
                System.out.printf("MOVE %d 0 0%n", sp.id);
            } else {
                System.out.printf("SHEAR %d %d%n", sp.id, sp.shearing);
            }
            return;
        }
        Player1.Shed sed = sheds.stream().filter(s -> s.owner == myId).min(Comparator.comparingDouble(s -> dist(s.x, sp.x, s.y, sp.y))).get();
        if (sp.wool > 0) {
            if (sed.x == Math.floor(sp.x) && sed.y == Math.floor(sp.y)) {
                System.out.printf("TRANSFER_WOOL 1 %d %d%n", sp.id, sp.wool);
            } else {
                System.out.printf(Locale.ROOT, "MOVE %d %f %f%n", sp.id, sed.x+0.5 - sp.x, sed.y+0.5 - sp.y);
            }
            return;
        }
        double dist = 1000;
        Player1.Sheep closesSheep = null;
        for (Player1.Sheep s : sheep
        ) {
            double tmpDist = dist(s.x, sp.x, s.y, sp.y);
            if (tmpDist < dist && s.wool > 0) {
                dist = tmpDist;
                closesSheep = s;
            }
        }
        if (closesSheep == null) {
            System.out.printf("MOVE %d 0 0%n", sp.id);
        } else if (isNearSheep(closesSheep, sp)) {
            System.out.printf("SHEAR %d %d%n", sp.id, sheep.indexOf(closesSheep) + 1);
        } else {
            System.out.printf(Locale.ROOT, "MOVE %d %f %f%n", sp.id, closesSheep.x - sp.x, closesSheep.y - sp.y);
        }
    }

    static double dist(double x1, double x2, double y1, double y2) {
        double deltaX = x1 - x2;
        double deltaY = y1 - y2;
        return sqrt(deltaX * deltaX + deltaY * deltaY);
    }

    static boolean isNearSheep(Player1.Sheep sheep, Player1.Shepherd shepherd) {
        return dist(sheep.x, shepherd.x, sheep.y, shepherd.y) < entityRadius;
    }
}
