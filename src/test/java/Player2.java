
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Player2 {
    static class Actor {
        float x,y;

        public Actor(float x, float y) {
            this.x = x;
            this.y = y;
        }
    }

    static class Shepherd extends Player1.Actor {
        int wool,shearing;

        public Shepherd(float x, float y,int wool,int shearing) {
            super(x, y);
            this.wool=wool;
            this.shearing=shearing;
        }
    }

    static class Sheep extends Player1.Actor {
        int wool;
        boolean isSheared;

        public Sheep(float x, float y, int wool, boolean isSheared) {
            super(x, y);
            this.wool = wool;
            this.isSheared = isSheared;
        }
    }

    static class Shed{
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
        int initialSheepWool=in.nextInt();
        int shepardMaxWool =in.nextInt();
        float entityRadius=in.nextFloat();
        float sheepSpeed1=in.nextFloat();
        float sheepSpeed2=in.nextFloat();
        float sheepSpeed3=in.nextFloat();
        float shepardSpeed=in.nextFloat();
        float dogSpeed=in.nextFloat();
        float dangerRadius=in.nextFloat();
        int barkCoolDown=in.nextInt();
        float barkRadius=in.nextFloat();
        int calmCoolDown=in.nextInt();
        int turns=in.nextInt();
        System.err.printf("%d %d %d %.6f %.6f %.6f %.6f %.6f %.6f %.6f %d %.6f %d %d%n", mapSize, initialSheepWool, shepardMaxWool,
            entityRadius, sheepSpeed1, sheepSpeed2, sheepSpeed3, shepardSpeed, dogSpeed, dangerRadius, barkCoolDown, barkRadius,
            calmCoolDown, turns);
        while (true) {
            int sheepCnt = in.nextInt();
            List<Sheep> sheep= new ArrayList<>();
            while (sheepCnt-->0){
                sheep.add(new Sheep(in.nextFloat(),in.nextFloat(),in.nextInt(), in.nextInt() != 0));
            }
            System.err.println("sheep");
            int myShepherdsCnt = in.nextInt();
            List<Shepherd> myShepherds = new ArrayList<>();
            while (myShepherdsCnt-->0){
                myShepherds.add(new Shepherd(in.nextFloat(),in.nextFloat(),in.nextInt(), in.nextInt()));
            }
            System.err.println("myS");
            int enemyShepherdsCnt = in.nextInt();
            List<Actor> enemyShepherds = new ArrayList<>();
            while (enemyShepherdsCnt-->0){
                enemyShepherds.add(new Actor(in.nextFloat(),in.nextFloat()));
            }
            System.err.println("enS");
            int myDogsCnt = in.nextInt();
            List<Actor> myDogs = new ArrayList<>();
            while (myDogsCnt-->0){
                myDogs.add(new Actor(in.nextFloat(),in.nextFloat()));
            }
            System.err.println("myD");
            int enemyDogsCnt = in.nextInt();
            List<Actor> enemyDogs = new ArrayList<>();
            while (enemyDogsCnt-->0){
                enemyDogs.add(new Actor(in.nextFloat(),in.nextFloat()));
            }
            System.err.println("eD");
            int shedsCnt = in.nextInt();
            List<Shed> sheds = new ArrayList<>();
            while (shedsCnt-->0){
                sheds.add(new Shed(in.nextInt(), in.nextInt(),in.nextInt(), in.nextInt(),in.nextInt(), in.nextInt()));
            }

            System.out.printf("MOVE 1 1 %d %d\n", random.nextInt(), random.nextInt());
        }
    }
}
