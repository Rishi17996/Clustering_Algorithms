import java.util.*;
import java.io.*;
import java.lang.*;

class kmeans_1 {
    public static void main(String xyz[]) throws IOException {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter the total number of elements : ");
        int ne = sc.nextInt();                                  // ne: total elements
        int nm = (int)(Math.sqrt(ne/2));                        // nm: number of means
        System.out.println("Number of means = "+nm);
        int[] ele = new int[ne];                                // ele[]: elements
        int[][] cluster = new int[nm][ne];                      // cluster[][]  
        System.out.println("Enter the elements :");
        for(int i=0;i<ne;i++) {
            ele[i]=sc.nextInt();
        }
    //Sorting the element array (Bubble sort)
        for(int z=0;z<ne;z++) {
            for(int y=0;y<ne-1;y++) {
                if(ele[y]>ele[y+1]) {
                    ele[y] = ele[y]+ele[y+1];
                    ele[y+1] = ele[y]-ele[y+1];
                    ele[y] = ele[y]-ele[y+1];
                }
            }
        }
        double[] mean = new double[nm];
        double[] temp_mean = new double[nm];
        /* int[] loc = new int[nm];                                //Mean location in ele[] (random means)        
        Random r = new Random();
        for(int i =0;i<nm;i++) {
            loc[i] = i*((int)(ne/nm)) + r.nextInt(ne/nm);
            mean[i] = ele[loc[i]];
        }*/
        
        //Initial means as means of ne/nm elements
        Double ab = new Double((ne/nm)+0.5);
        int abc = ab.intValue();
        for(int i=0;i<nm;i++) {
            int s = 0;
            int c = 0;
            for(int j=0;j<abc;j++) {
                if(((i*abc)+j)<ne) {
                    s = s + ele[(i*abc)+j];
                    c++;
                }
            }
            mean[i] = (double)s/(double)c;
        }
        
        int[] count = new int[nm];
        for(int i=0;i<nm;i++) {
            count[i]=0; 
        }
        functions f = new functions();
        for(int iterate=1;;iterate++) {
            System.out.println("After updating means : ");
            f.print_means(mean,nm);
            if(f.check_means(mean,temp_mean,nm)) {
                System.out.println();
                System.out.println("Iteration "+iterate+" :");
                f.print_means(mean,nm);
                f.allot1(ele,cluster,count,mean,nm,ne);
                f.display(cluster,count,nm);
                f.update_means(cluster,count,nm,mean,temp_mean);
                f.update_count(count,nm);
            }
            else {
                break;
            }
        }
    }
}

class functions {
    
    boolean check_means(double[] mean,double[] temp_mean,int nm) {
        int c = 0;
        for(int i=0;i<nm;i++) {
            if(mean[i]==temp_mean[i]) {
                c++;
            }
        }
        if(c==nm) {
            return false;
        }
        else {
            return true;
        }
    }
    
    void print_means(double[] mean,int nm) {
        for(int i =0;i<nm;i++) {
            System.out.println("Mean "+(i+1)+" = "+mean[i]);
        }
    }
    
    void allot1(int[] ele,int[][] cluster,int[] count,double[] mean,int nm,int ne) {
        for(int i=0;i<ne;i++) {
            int select = 0;
            for(int k=1;k<nm;k++) {
                if((double)(Math.abs(ele[i]-mean[select]))<=(double)(Math.abs(ele[i]-mean[k]))) {
                    select = select;
                }
                else {
                    select = k;
                }
            }
            cluster[select][count[select]]=ele[i];
            count[select]++;
        }
    }
    
    void display(int[][] cluster,int[] count,int nm) {
        for(int z=0;z<nm;z++) {
            System.out.print("Cluster "+(z+1)+" : ");
            for(int i=0;i<count[z];i++) {
                System.out.print(cluster[z][i]+" ");
            }
            System.out.println();
        }
    }
    
    void update_means(int[][] cluster,int[] count,int nm,double[] mean,double[] temp_mean) {
        for(int j=0;j<nm;j++) {
            temp_mean[j] = mean[j];
        }
        for(int i=0;i<nm;i++) {
            int sum = 0;
            for(int j=0;j<count[i];j++) {
                sum = sum + cluster[i][j];
            }
            mean[i] = (double)sum/(double)count[i];
        }
    }
    
    void update_count(int[] count,int nm) {
        for(int i=0;i<nm;i++) {
            count[i] = 0;
        }
    }
}