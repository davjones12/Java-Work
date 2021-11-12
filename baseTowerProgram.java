 import java.util.*;
/**
 * 6-24-2020
 * @author David.Jones
 * This program is designed to place cell phone on a road at the most optimal location based
 * on where houses are located on a road.
 */
 
 /* Driver Class */
class baseTowerProgram {
    /* Set road length */     
    static int[] createRoad(int length)
    {
        int[] newRoad = new int[length];
        
        return newRoad;
    }
    
    /* Set house location on road */
    static int[] placeHouse(int[] road, int houseLocation)
    {
        road[houseLocation] = 1;
        
        return road;
    }
    
    /* Set base tower location on road */
    static int[] placeBase(int[] baseLocations, int baseLocation)
    {
        baseLocations[baseLocation] = 1;
        
        return baseLocations;
    }
    
    /* Function to figure out where to place the base tower */
    static void basePlacement(int[] houses)
    {
        int[] baseLocations = new int[houses.length];
        
        int basewidth = 0;
        int c = 0;
        
        for(int x = 0; x < houses.length; x++)
        {
            if((basewidth == 8))
            {
                c = x - 4;
                baseLocations = placeBase(baseLocations, c);
                basewidth = 0;
            }
            else if((x == houses.length - 1) && ((basewidth > 0) || (houses[x] == 1)))
            {
                c = x - 2;
                baseLocations = placeBase(baseLocations, c);
            }
            else if((houses[x] == 1) && (basewidth == 0))
            {
                basewidth = 0;
                basewidth++;    
            }
            else if(((houses[x] == 1)|| (houses[x] == 0)) && (basewidth > 0))
            {
                basewidth++;
            }
        }
        
        System.out.println("HOUSE LOCATIONS ON ROAD:");
        for(int x = 0; x < houses.length; x++)
        {
            if(houses[x] == 1)
            {
                System.out.println(x + " mile marker");
            }
        }
        
        System.out.println("\nCELL PHONE BASE STATION LOCATIONS:");
        for(int x = 0; x < houses.length; x++)
        {
            if(baseLocations[x] == 1)
            {
                System.out.println(x + " mile marker");
            }
        }
    }

    /* Main Function */
    public static void main(String args[]) 
    {
            // Creating a road of a certain length
            int[] road = createRoad(25);
            int[] houses = new int[road.length];
            
            // Place houses at the following mile markers on the road
            houses = placeHouse(road, 4);
            houses = placeHouse(road, 6);
            houses = placeHouse(road, 9);
            houses = placeHouse(road, 12);
            houses = placeHouse(road, 13);
            houses = placeHouse(road, 14);
            houses = placeHouse(road, 17);
            houses = placeHouse(road, 24);
            
            // Find optimal locations
            basePlacement(houses);
    }
}
