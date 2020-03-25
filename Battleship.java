import java.util.Random;

public class Battleship {

    private static boolean[][] table = new boolean[10][10];

    public static void main(String[] args){
        // Starta BStable,
        BSTable bsTable = new BSTable();
        bsTable.setShips();
        bsTable.printTable();
        betterGuesses(bsTable);
        bsTable.printTable();
        System.out.println("Ships sunk in " + bsTable.getBombCount() + " tries");

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
            System.out.println(s);
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

    



    
}