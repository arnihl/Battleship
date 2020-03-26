import java.util.Random;

public class Battleship {

    private static boolean[][] table = new boolean[10][10];

    public static void main(String[] args){
        // Starta BStable,
        int count = 0; 
        BSTable bsTable = new BSTable();
        int k = 100;
        for(int i = 0; i < k; i++){
            table = new boolean[10][10];
            bsTable.resetTable();
            bsTable.setShips();
            randomGuesses(bsTable);
            count += bsTable.getBombCount();
        }
        System.out.println("Random guesses average: "
         + (count/k) + " in " + k + " tests");
        count = 0; 
        for(int i = 0; i < k; i++){
            table = new boolean[10][10];
            bsTable.resetTable();
            bsTable.setShips();
            betterGuesses(bsTable);
            count += bsTable.getBombCount();
        }
        table = new boolean[10][10];
        System.out.println("Better guesses average: "
         + (count/k) + " in " + k + " tests");
        count = 0; 
        for(int i = 0; i < k; i++){
            table = new boolean[10][10];
            bsTable.resetTable();
            bsTable.setShips();
            divide(bsTable);
            count += bsTable.getBombCount();
        }
        System.out.println("Divide and conquer average: "
         + (count/k) + " in " + k + " tests");
    }

    // gráðug aðferð, notar eintóm gisk
    public static void randomGuesses(BSTable bsTable){
        for(int i = 0; i < 100; i++){
            int y = (int) (Math.random() * 10);
            int x = (int) (Math.random() * 10);
            if(table[x][y]){
                i--;
                continue;
            }
            table[x][y] = true;
            String s = bsTable.dropBomb(x, y);
            if(s.equals("LOST")){
                break;
            }

        }

        
    }

    // önnur gráðug aðferð. 
    // Giskar fyrst en leitar skynsamlegar 
    // þegar sprengja lendir á skipi. 
    public static void betterGuesses(BSTable bsTable){
        // byrjar eins og randomGuesses
        // notum hjálparfall þegar við sprengjum skip. 
        for(int i = 0; i < 100; i++){
            if(bsTable.getShipCount() <= 0){
                break;
            }
            int y = (int) (Math.random() * 10);
            int x = (int) (Math.random() * 10);
            if(table[x][y]){
                i--;
                continue;
            }
            table[x][y] = true;
            String s = bsTable.dropBomb(x, y);
            if(s.equals("HIT")){
                String t = workAround(x,y, bsTable);
                if(t.equals("LOST")){
                    break;
                }
            } 
        }
    }

    public static String workAround(int x, int y, BSTable bsTable){
        int originX = x;
        int originY = y;
        int count = 0; 

        // tjékka hvort það virki að hækka x
        while(x < 9){
            x++;
            if(!table[x][y]){
                String t = bsTable.dropBomb(x, y);
                table[x][y] = true;
                if(t.equals("HIT")){
                    count++;
                }
                else if(t.equals("LOST")){
                    return "LOST";
                } else {
                    break;
                }
            } else {
                break;
            }
        }
        if(count == 4) return " ";
        // tjékka hvort það virki að lækka x
        x = originX;
        while(x > 0){
            x--;
            if(!table[x][y]){
                table[x][y] = true;
                String t = bsTable.dropBomb(x, y);
                if(t.equals("HIT")){
                    count++;
                }
                else if(t.equals("LOST")){
                    return "LOST";
                } else {
                    break;
                }
            } else {
                break;
            }
        }
        if(count == 4) return " ";
        x = originX;
        // tjékka hvort það virki að hækka y
        while(y<9){
            y++;
            if(!table[x][y]){
                table[x][y] = true;
                String t = bsTable.dropBomb(x, y);
                if(t.equals("HIT")){
                    count++;
                }
                else if(t.equals("LOST")){
                    return "LOST";
                } else {
                    break;
                }
            } else {
                break;
            }
        }
        y = originY;
        if(count == 4) return " ";
        // tjékka hvort það virki að lækka y
        while(y>0){
            y--;
            if(!table[x][y]){
                table[x][y] = true;
                String t = bsTable.dropBomb(x, y);
                if(t.equals("HIT")){
                    count++;
                }
                else if(t.equals("LOST")){
                    return "LOST";
                } else {
                    break;
                }
            } else {
                break;
            }
        }

        return " ";
            
    }

    // brýtur borðið í fjóra hluta og leitar að skipum 
    // í hornalínu á hverjum hlut. 
    public static void divide(BSTable bsTable){
        // Skipta borði í fjóra hluta
        // kalla á fall sem leitar í hornalínunni 
        // í hverjum fjórðungi. 
        // Ef strengnum "LOST" er skilað: hætta
        int x = 0; 
        int y = 0;  
        String first = searchQuarter(bsTable, x, y);
        if(first.equals("LOST")){
            return;
        }
        String second = searchQuarter(bsTable, x, y+5);
        if(second.equals("LOST")){
            return;
        }
        String third = searchQuarter(bsTable, x+5, y);
        if(third.equals("LOST")){
            return;
        }
        String fourth = searchQuarter(bsTable, x+5, y+5);
    }

    public static String searchQuarter(BSTable bsTable, int x, int y){
        // Leita í hornalínunni frá x,y til x+5,y+5
        // Ef ekki er búið að skjóta í x,y áður þá skjóta. 
        //      ef skip finnst; leita í kringum skotstað með hjálparfallinu "workaround"
        // Skila streng "LOST" ef öll skip eru sokkin. 
        for(int i = 0; i <5; i++){
            String t = bsTable.dropBomb(x+i, y+i);
            if(t.equals("HIT")){
                String b = workAround(x+i, y+i, bsTable);
                if(b.equals("LOST")){
                    return "LOST";
                }
            }
        }
        return " "; 
    }



    
}