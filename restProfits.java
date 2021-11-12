import java.util.*;
/**
 * 7-8-2020
 * @author David.Jones
 */
public class restProfits
{
    /* Finds the maximum profit; takes in a array of restaurant locations, expected
    profits at each location, and distance (k) */
    static int findProfits(int[] arraylocations, int[] profits, int distance)
    {
        int[] profitArray = new int[arraylocations.length];
        int[] P = profits;
        int temp = 0;
        int profitMax = 0;
        int[] locations = arraylocations;
        
        for(int i = 1; i < arraylocations.length; i++)
        {
            profitArray[i] = 0;
        }
        
        for(int i = 2; i < arraylocations.length; i++)
        {
            for(int j = 1; j < i - 1; j++)
            {
                temp = profitArray[j] + funcMax(locations[i], locations[j], distance) * P[i];
                
                if(temp > profitArray[i])
                {
                    profitArray[i] = temp;
                }
            }
            
            if(profitArray[i] < P[i])
            {
                profitArray[i] = P[i];
            }
        }
        
        profitMax = profitArray[arraylocations.length - 1];
        
        return profitMax;
    }
    
    /* Function to find the max */
    static int funcMax(int ai, int bj, int c)
    {
        // c is how many miles the restaurants should be apart
        if((ai - bj) < c)
        {
            return 0;
        }
        else if((ai - bj) >= c)
        {
            return 1;
        }
        else
        {
            return 1;
        }
    }
    
    /* Main driver function */
    public static void main (String[] args) 
    { 
        int number;
        System.out.println("Please enter how many items you want in your array:");
        Scanner console = new Scanner(System.in);
        number = console.nextInt();
        int[] array1 = new int[number];
        int[] array2 = new int[number];

        System.out.println("Please enter the location of each restaurant:");
        int currnum = 0;
        for(int g = 0; g < number; g++)
        {
            currnum = console.nextInt();
            array1[g] = currnum;
        }
        
        System.out.println("Please enter the expected profit of each restaurant:");
        int currnum2 = 0;
        for(int g = 0; g < number; g++)
        {
            currnum2 = console.nextInt();
            array2[g] = currnum2;
        }
        
        System.out.println("Please enter the minimum distance (k):");
        int currnum3 = 0;
        currnum3 = console.nextInt();
        
        //int[] array1 = {1, 4, 5, 7, 8};
        //int[] array2 = {100, 100, 100, 100, 100};

        int ans = findProfits(array1, array2, currnum3);

        System.out.println("MAX PROFIT: " + ans);
    } 
}
