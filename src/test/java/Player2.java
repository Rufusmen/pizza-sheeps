import static java.lang.Math.sqrt;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.Scanner;

public class Player2 {

    static float entityRadius;
    static int myId;

    static class Actor {

        float x, y;
        int owner;

        public Actor(float x, float y, int owner) {
            this.owner = owner;
            this.x = x;
            this.y = y;
        }
    }

    static class Shepherd extends Actor {

        int wool, shearing;

        public Shepherd(float x, float y, int wool, int shearing, int owner) {
            super(x, y, owner);
            this.wool = wool;
            this.shearing = shearing;
        }
    }

    static class Sheep extends Actor {

        int wool;
        boolean isSheared;

        public Sheep(float x, float y, int wool, boolean isSheared) {
            super(x, y, -1);
            this.wool = wool;
            this.isSheared = isSheared;
        }
    }

    static class Shed {

        int x;
        int y;
        int owner;
        int wool;
        int myDogs;
        int enemyDogs;

        public Shed(int x, int y, int owner, int wool, int myDogs, int enemyDogs) {
            this.x = x;
            this.y = y;
            this.owner = owner;
            this.wool = wool;
            this.myDogs = myDogs;
            this.enemyDogs = enemyDogs;
        }

    }

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        Random random = new Random(0);
        myId = in.nextInt();
        int mapSize = in.nextInt();
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
        int turns = in.nextInt();

        int sheepCnt = in.nextInt();
        int shepherdsCnt = in.nextInt();
        int dogsCnt = in.nextInt();
        int shedsCnt = in.nextInt();
        int barkTime = 0;
        while (true) {
            //int sheepCnt = in.nextInt();
            List<Sheep> sheep = new ArrayList<>();
            for (int i = 0; i < sheepCnt; i++) {
                sheep.add(new Sheep(in.nextFloat(), in.nextFloat(), in.nextInt(), in.nextInt() != 0));
            }
            //int myShepherdsCnt = in.nextInt();
            List<Shepherd> shepherds = new ArrayList<>();
            for (int i = 0; i < shepherdsCnt; i++) {
                shepherds.add(new Shepherd(in.nextFloat(), in.nextFloat(), in.nextInt(), in.nextInt(), in.nextInt()));
            }
            //int enemyShepherdsCnt = in.nextInt();
            //int enemyDogsCnt = in.nextInt();
            List<Actor> dogs = new ArrayList<>();
            for (int i = 0; i < dogsCnt; i++) {
                dogs.add(new Actor(in.nextFloat(), in.nextFloat(), in.nextInt()));
            }
            //int shedsCnt = in.nextInt();
            List<Shed> sheds = new ArrayList<>();
            for (int i = 0; i < shedsCnt; i++) {

                sheds.add(new Shed(in.nextInt(), in.nextInt(), in.nextInt(), in.nextInt(), in.nextInt(), in.nextInt()));
            }
            for (int i = 0; i < shepherdsCnt; i++) {
                Shepherd s = shepherds.get(i);
                if (s.owner == myId) {
                    playShepard(s, i, sheds, sheep);
                }
            }
            for (int i = 0; i < dogsCnt; i++) {
                Actor d = dogs.get(i);
                if (d.owner == myId) {

                    if (barkTime == 0) {
                        System.out.printf("BARK %d%n", i);
                    } else {
                        System.out.printf("MOVE 0 %d %d %d%n", i, random.nextInt(), random.nextInt());
                    }
                }
            }
            if(barkTime==0)barkTime=barkCoolDown+1;
        }
    }

    static void playShepard(Shepherd sp, int id, List<Shed> sheds, List<Sheep> sheep) {
        if (sp.shearing != 0) {
            Sheep s = sheep.get(sp.shearing - 1);
            if (s.wool <= 0) {
                System.out.printf("SHEAR 0 %d %d%n", id, sp.shearing);
            } else {
                System.out.printf("SHEAR 1 %d %d%n", id, sp.shearing);
            }
            return;
        }
        Shed sed = sheds.stream().filter(s -> s.owner == myId).findFirst().get();
        if (sp.wool > 0) {
            if (sed.x == Math.floor(sp.x) && sed.y == Math.floor(sp.y)) {
                System.out.printf("TRANSFER_WOOL 1 %d %d%n", id, sp.wool);
            } else {
                System.out.printf(Locale.ROOT, "MOVE 1 %d %f %f%n", id, sed.x - sp.x, sed.y - sp.y);
            }
            return;
        }
        double dist = 1000;
        Sheep closesSheep = null;
        for (Sheep s : sheep
        ) {
            double tmpDist = dist(s.x, sp.x, s.y, sp.y);
            if (tmpDist < dist && s.wool > 0) {
                dist = tmpDist;
                closesSheep = s;
            }
        }
        if (closesSheep == null) {
            System.out.printf("MOVE 1 %d 0 0", id);
        } else if (isNearSheep(closesSheep, sp)) {
            System.out.printf("SHEAR 1 %d %d%n", id, sheep.indexOf(closesSheep) + 1);
        } else {
            System.out.printf(Locale.ROOT, "MOVE 1 %d %f %f%n", id, closesSheep.x - sp.x, closesSheep.y - sp.y);
        }
    }

    static double dist(double x1, double x2, double y1, double y2) {
        double deltaX = x1 - x2;
        double deltaY = y1 - y2;
        return sqrt(deltaX * deltaX + deltaY * deltaY);
    }

    static boolean isNearSheep(Sheep sheep, Shepherd shepherd) {
        return dist(sheep.x, shepherd.x, sheep.y, shepherd.y) < entityRadius;
    }
}
