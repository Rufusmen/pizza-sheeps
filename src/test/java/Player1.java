import static java.lang.Math.sqrt;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Player1 {

    static float entityRadius;

    static class Actor {

        float x, y;

        public Actor(float x, float y) {
            this.x = x;
            this.y = y;
        }
    }

    static class Shepherd extends Actor {

        int wool, shearing;

        public Shepherd(float x, float y, int wool, int shearing) {
            super(x, y);
            this.wool = wool;
            this.shearing = shearing;
        }
    }

    static class Sheep extends Actor {

        int wool;
        boolean isSheared;

        public Sheep(float x, float y, int wool, boolean isSheared) {
            super(x, y);
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
        System.err.printf("%d %d %d %.6f %.6f %.6f %.6f %.6f %.6f %.6f %d %.6f %d %d%n", mapSize, initialSheepWool, shepardMaxWool,
            entityRadius, sheepSpeed1, sheepSpeed2, sheepSpeed3, shepardSpeed, dogSpeed, dangerRadius, barkCoolDown, barkRadius,
            calmCoolDown, turns);
        while (true) {
            int sheepCnt = in.nextInt();
            List<Sheep> sheep = new ArrayList<>();
            while (sheepCnt-- > 0) {
                sheep.add(new Sheep(in.nextFloat(), in.nextFloat(), in.nextInt(), in.nextInt() != 0));
            }
            int myShepherdsCnt = in.nextInt();
            List<Shepherd> myShepherds = new ArrayList<>();
            while (myShepherdsCnt-- > 0) {
                myShepherds.add(new Shepherd(in.nextFloat(), in.nextFloat(), in.nextInt(), in.nextInt()));
            }
            int enemyShepherdsCnt = in.nextInt();
            List<Actor> enemyShepherds = new ArrayList<>();
            while (enemyShepherdsCnt-- > 0) {
                enemyShepherds.add(new Actor(in.nextFloat(), in.nextFloat()));
            }
            int myDogsCnt = in.nextInt();
            List<Actor> myDogs = new ArrayList<>();
            while (myDogsCnt-- > 0) {
                myDogs.add(new Actor(in.nextFloat(), in.nextFloat()));
            }
            int enemyDogsCnt = in.nextInt();
            List<Actor> enemyDogs = new ArrayList<>();
            while (enemyDogsCnt-- > 0) {
                enemyDogs.add(new Actor(in.nextFloat(), in.nextFloat()));
            }
            int shedsCnt = in.nextInt();
            List<Shed> sheds = new ArrayList<>();
            while (shedsCnt-- > 0) {
                sheds.add(new Shed(in.nextInt(), in.nextInt(), in.nextInt(), in.nextInt(), in.nextInt(), in.nextInt()));
            }
            Shepherd sp = myShepherds.get(0);
            System.err.println(sp.wool);
            if (sp.shearing != 0) {
                Sheep s = sheep.get(sp.shearing-1);
                if(s.wool<=0)System.out.printf("SHEAR 0 0 %d%n",sp.shearing);
                else System.out.println("");
                continue;
            }
            Shed sed= sheds.stream().filter(s -> s.owner==0).findFirst().get();
            if(sp.wool>0){
                if(sed.x == Math.floor(sp.x) && sed.y == Math.floor(sp.y)){
                    System.out.printf("TRANSFER_WOOL 1 0 %d%n",sp.wool);
                }
                else {
                    System.out.println("MOVE 1 0 -1 -1");
                }
                continue;
            }
            Sheep se = sheep.stream().filter(s -> isNearSheep(s, sp) && s.wool>0).findFirst().orElse(null);
            if (se != null) {
                System.out.printf("SHEAR 1 0 %d%n", sheep.indexOf(se) + 1);
            } else {
                System.out.println("MOVE 1 0 1 1");
            }
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
