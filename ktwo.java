import java.util.*;
import java.io.*;
import java.lang.*;

class ktwo {
    public static void main(String args[]) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter the total number of elements : ");
        int ne = sc.nextInt();
        int nm = (int)(Math.sqrt(ne/2));
        System.out.println("Number of means = "+nm);
        int ele[][] = new int[ne][2];
        int[][][] cluster = new int[nm][ne][2];
        int[] count = new int[nm];
        System.out.println("Enter the elements :");
        for(int i=0;i<ne;i++) {
            for(int j=0;j<2;j++) {
                ele[i][j] = sc.nextInt();
            }
        }
        double[][] mean = new double[nm][2];
        double[][] temp_mean = new double[nm][2];
        for(int i=0;i<nm;i++) {
            for(int j=0;j<2;j++) {
                temp_mean[i][j] = 0.0;
            }
        }
        double[] dfo = new double[ne]; //Distance From Origin
        functions f = new functions();
        f.cal_dist(ele,dfo,ne);
        f.sort(ele,dfo,ne);
        System.out.println();
        System.out.println("In ascending order :");
        for(int i=0;i<ne;i++) {
            System.out.println("("+ele[i][0]+","+ele[i][1]+")  (DFO = "+dfo[i]+")");
        }
        f.init_means(ele,mean,ne,nm);
        System.out.println();
        System.out.println("Initial means :");
        f.disp_means(mean,nm);
        for(int it=1;;it++) {
            if(f.equal(temp_mean,mean,nm)) {
                System.out.println();
                System.out.println("Iteration "+it+" :");
                f.allot(ele,cluster,count,mean,nm,ne); 
                f.disp_clu(cluster,count,nm);
                f.update_means(cluster,count,nm,mean,temp_mean);
                f.update_count(count,nm);
                f.disp_means(mean,nm);
            }
            else {
                break;
            }
        }
    }
}

class functions {
    void disp_means(double[][] mean,int nm) {
        for(int i=0;i<nm;i++) {
            System.out.println("Mean "+(i+1)+": ("+mean[i][0]+","+mean[i][1]+")");
        }
    }
    
    void cal_dist(int[][] ele,double[] dfo,int ne) {
        for(int i=0;i<ne;i++) {
            int a2 = ele[i][0]*ele[i][0];
            int b2 = ele[i][1]*ele[i][1];
            int s = a2+b2;
            dfo[i] = (double)Math.sqrt(s);
        }
    }
    
    void sort(int[][] ele,double[] dfo,int ne) {
        int temp = 0; 
        double tempd;
        for(int i=0;i<ne;i++) {
            for(int j=0;j<ne-1;j++) {
                if(dfo[j]>dfo[j+1]) {
                    tempd = dfo[j];
                    dfo[j] = dfo[j+1];
                    dfo[j+1] = tempd;
                    temp = ele[j][0];
                    ele[j][0] = ele[j+1][0];
                    ele[j+1][0] = temp;
                    temp = ele[j][1];
                    ele[j][1] = ele[j+1][1];
                    ele[j+1][1] = temp;
                }
            }
        }    
    }
    
    void init_means(int[][] ele,double[][] mean,int ne, int nm) {
        int i;
        Double ab = new Double(ne/nm);
        int abc = (int)Math.round(ab);
        int a = ne%nm;
        for(i=0;i<nm-a;i++) {
            int x = 0;
            int y = 0;
            int c = 0;
            for(int j=0;j<abc;j++) {
                if(((i*abc)+j)<ne) {
                    x = x + ele[(i*abc)+j][0];
                    y = y + ele[(i*abc)+j][1];
                    c++;
                }
            }
            //System.out.println(c);
            mean[i][0] = (double)x/(double)c;
            mean[i][1] = (double)y/(double)c;
        }
        int b = i;
        for(i=b;i<nm;i++) {
            int x = 0;
            int y = 0;
            int c = 0;
            for(int j=0;j<=abc;j++) {
                if(((i*abc)+j)<ne) {
                    x = x + ele[(i*abc)+j][0];
                    y = y + ele[(i*abc)+j][1];
                    c++;
                }
            }
            //System.out.println(c);
            mean[i][0] = (double)x/(double)c;
            mean[i][1] = (double)y/(double)c;
        }
    }
    
    boolean equal(double[][] temp_mean,double[][] mean,int nm) {
        int c = 0;
        for(int z=0;z<nm;z++) {
            for(int y=0;y<2;y++) {
                if(temp_mean[z][y]==mean[z][y]) {
                    c++;
                }
            }
        }
        if(c==(nm*2)) {
            return false;
        }
        else {
            return true;
        }
    }
    
    void allot(int[][] ele,int[][][] cluster,int[] count,double[][] mean,int nm, int ne) {
        int select;
        for(int i=0;i<ne;i++) {
            select = 0;
            for(int k=1;k<nm;k++) {
                if((double)(Math.abs(dist(ele[i][0],ele[i][1],mean[select][0],mean[select][1])))<=(double)(Math.abs(dist(ele[i][0],ele[i][1],mean[k][0],mean[k][1])))) {
                    select = select;
                }
                else {
                    select = k;
                }
            }
            cluster[select][count[select]][0]=ele[i][0];
            cluster[select][count[select]][1]=ele[i][1];
            count[select]++;
        }
    }
    
    double dist(double x1,double y1,double x2,double y2) {
        double d = 0;
        double a = (x1-x2)*(x1-x2);
        double b = (y1-y2)*(y1-y2);
        d = (double)(Math.sqrt(a+b));
        return d;
    }
    
    void disp_clu(int[][][] cluster,int[] count,int nm) {
        for(int z=0;z<nm;z++) {
            System.out.print("Cluster "+(z+1)+" : ");
            for(int i=0;i<count[z];i++) {
                System.out.print("("+cluster[z][i][0]+","+cluster[z][i][1]+") ");
            }
            System.out.println();
        }
    }
    
    void update_means(int[][][] cluster,int[] count,int nm,double[][] mean,double[][] temp_mean) {
        for(int j=0;j<nm;j++) {
            temp_mean[j][0] = mean[j][0];
            temp_mean[j][1] = mean[j][1];
        }
        for(int i=0;i<nm;i++) {
            int x = 0;
            int y = 0;
            for(int j=0;j<count[i];j++) {
                x = x + cluster[i][j][0];
                y = y + cluster[i][j][1];
            }
            mean[i][0] = (double)x/(double)count[i];
            mean[i][1] = (double)y/(double)count[i];
        }
    }
    
    void update_count(int[] count,int nm) {
        for(int i=0;i<nm;i++) {
            count[i] = 0;
        }
    }
}
