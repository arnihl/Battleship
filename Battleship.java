

public class Battleship {

    private static boolean[][] table = new boolean[10][10];

    public static void main(String[] args){

        BSTable bsTable = new BSTable();
       /* bsTable.setShips();
        bsTable.printTable();
        divide(bsTable);
        bsTable.printTable();
        System.out.println("Bombs: " + bsTable.getBombCount() + " and ships: " + bsTable.getShipCount());
        */
        int k = 20;
        randomGuessTest(k, bsTable);
        betterGuessTest(k, bsTable);
        divideAndConquerTest(k, bsTable);
    
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
        int horizontalStartY = 0;
        int horizontalEndY = 0;

        int verticalStartX = 0;
        int verticalEndX = 0;

        int count = 0; 

        // tjékka hvort það virki að hækka x
        while(x < 9){
            x++;
            if(!table[x][y]){
                String t = bsTable.dropBomb(x, y);
                table[x][y] = true;
                if(t.equals("HIT")){
                    count++;
                    verticalEndX++;
                }
                else if(t.equals("LOST")){
                    return "LOST";
                } else {
                    break;
                }
            } else {
                continue;
            }
        }

        // tjékka hvort það virki að lækka x
        x = originX;
        while(x > 0){
            x--;
            if(!table[x][y]){
                table[x][y] = true;
                String t = bsTable.dropBomb(x, y);
                if(t.equals("HIT")){
                    verticalStartX--;
                    count++;

                }
                else if(t.equals("LOST")){
                    return "LOST";
                } else {
                    break;
                }
            } else {
                continue;
            }
        }

        x = originX;
        // tjékka hvort það virki að hækka y
        while(y<9){
            y++;
            if(!table[x][y]){
                table[x][y] = true;
                String t = bsTable.dropBomb(x, y);
                if(t.equals("HIT")){
                    horizontalEndY++;
                    count++;
                }
                else if(t.equals("LOST")){
                    return "LOST";
                } else {
                    break;
                }
            } 
            else {
                continue;
            }
            
        }

        
        y = originY;
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
                continue;
            }
        }
        // ef sprengjutalningin er hærri en 4
        // þýðir það að við höfum samliggjandi skip 
        // einhverstaðar.
        // skipin geta bara verið á endum þess skips sem við sprengdum
        // þannig að við sendum þau hnit í workaround fallið aftur
        // uncommenta þetta ef við viljum leita að aðliggjandi skipum
        /*if(count>=5){
            String b = "";
            if(verticalEndX != 0){
                b = workAround(originX+verticalEndX, originY, bsTable);
                if(b.equals("LOST")){
                    return "LOST";
                }
            } 
            if(verticalStartX != 0){
                b = workAround(originX+verticalStartX, originY, bsTable);
                if(b.equals("LOST")){
                    return "LOST";
                }
            }
            if(horizontalEndY != 0){
                b = workAround(originX, originY+horizontalEndY, bsTable);
                if(b.equals("LOST")){
                    return "LOST";
                }
            }
            if(horizontalStartY != 0){
                b = workAround(originX, originY+horizontalStartY, bsTable);
                if(b.equals("LOST")){
                    return "LOST";
                }
            }

        }*/
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
            if(table[x+i][y+i]){
                String b = workAround(x+i, y+i, bsTable);
                if(b.equals("LOST")){
                    return "LOST";
                } else {
                    continue;
                }
            }
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

    // Eftirfarandi eru test föll, sem testa ákveðnar
    // aðferðir k sinnum. 

    public static void randomGuessTest(int k, BSTable bsTable){
        int count = 0; 
        int errorCount = 0;
        for(int i = 0; i < k; i++){
            table = new boolean[10][10];
            bsTable.resetTable();
            bsTable.setShips();
            randomGuesses(bsTable);
            count += bsTable.getBombCount();
            if(bsTable.getShipCount() != 0){
                errorCount++;
            }
        }
        System.out.println("Random guesses average: "
         + (count/k) + " in " + k + " tests" + " and error rate: " + errorCount);
    }

    public static void betterGuessTest(int k, BSTable bsTable){
        int count = 0;
        int errorCount = 0;
        for(int i = 0; i < k; i++){
            table = new boolean[10][10];
            bsTable.resetTable();
            bsTable.setShips();
            betterGuesses(bsTable);
            count += bsTable.getBombCount();
            if(bsTable.getShipCount() != 0){
                errorCount++;
            }
        }
        table = new boolean[10][10];
        System.out.println("Better guesses average: "
         + (count/k) + " in " + k + " tests" + " and error rate: " + errorCount);
    }

    public static void divideAndConquerTest(int k, BSTable bsTable){
        int count = 0; 
        int errorCount = 0;
        for(int i = 0; i < k; i++){
            table = new boolean[10][10];
            bsTable.resetTable();
            bsTable.setShips();
            divide(bsTable);
            count += bsTable.getBombCount();
            if(bsTable.getShipCount() != 0){
                errorCount++;
            }
        }
        System.out.println("Divide and conquer average: "
         + (count/k) + " in " + k + " tests" + " and error rate: " + errorCount);
    }



    
}