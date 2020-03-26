import java.util.Arrays;

public class BSTable {

    private String[][] table;
    private int shipCount;
    private int bombCount;
    // BStable heldur utan um borð og hvar skip eru staðsett

    public BSTable(){
        this.table = new String[10][10];
        for(int i = 0; i < 10; i++){
            Arrays.fill(this.table[i], " ");
        }
        this.shipCount = 0;
        this.bombCount = 0;
    }

    public void setShips(){
        // Setja þrjú random skip, 5 að lengd;
        shipCount = 15;
        for(int i = 0; i < 3; i++){
            // flippa pening uppá hvort skip verði lóðrétt eða lárétt
            // Ef lóðrétt
            //     finna random y gildi á bili [0,10)
            //     finna random x gildi á bili [0,5)
            //     Ef skip skarast: endurtaka
            //     Annars: Setja niður skip. 
            // Ef Lárétt 
            //     finna random y gildi á bili [0,5)
            //     finna random x gildi á bili [0,10)
            //     Ef skip skarast: endurtaka
            //     Annars: Setja niður skip. 
            boolean lodrett = flipACoin();
            if(lodrett){
                int y = (int) (Math.random()*10);
                int x = (int) (Math.random()*5);
                if(skipSkarast(x,y,lodrett)){
                    i--;
                    continue;
                } else {
                    setjaNidurSkip(x,y,lodrett);
                }
            }
            else {
                int y = (int) (Math.random()*5);
                int x = (int) (Math.random()*10);
                if(skipSkarast(x,y,lodrett)){
                    i--;
                    continue;
                } else {
                    setjaNidurSkip(x,y,lodrett);
                }
            }
        }

        


    }

    private void setjaNidurSkip(int x, int y, boolean lodrett){
        // setja niður strenginn "1" í table, 
        //  fimm sinnum niður ef lodrett
        //  fimm sinnum til hægri annars. 
        for(int i = 0; i < 5; i++){
            this.table[x][y] = "1";
            if(lodrett) x++;
            else y++;
        }
    }

    private boolean skipSkarast(int x, int y, boolean lodrett){
        // skila true ef skip er fyrir

        for(int i = 0; i < 5; i++){
            if(this.table[x][y].equals("1")){
                return true;
            }
            if(lodrett){
                x++;
            }
            else {
                y++;
            }
        }
        return false; 
    }

    public boolean flipACoin(){
        double rand = Math.random();
        boolean b = (rand > 0.5) ? true : false;
        return b;
    }

    public String dropBomb(int i, int j){
        // segja hvort sprengja hafi hitt skip eða ekki. 
        if(this.table[i][j].equals("1")){
            this.table[i][j] = "X";
            this.shipCount--;
            this.bombCount++;
            if(this.shipCount <= 0) {
                return "LOST";
            }
            return "HIT";
        }
        this.table[i][j] = "X";
        this.bombCount++;
        return "MISS";
    }

    public void resetTable(){
        for(int i = 0; i < 10; i++){
            Arrays.fill(this.table[i], " ");
        }
        this.shipCount = 0;
        this.bombCount = 0;
    }

    public void printTable(){
        for(int i = 0; i < this.table.length; i++){
            System.out.println("-----------------------------------------");
            for(int j = 0; j < this.table[0].length; j++){
                System.out.print("| " + this.table[i][j] + " ");
            }
            System.out.print("|");
            System.out.println("");
            
        }
        System.out.println("-----------------------------------------");
    }

    public int getShipCount(){
        return this.shipCount;
    }

    public int getBombCount(){
        return this.bombCount;
    }

}