package model;

public class Tool {
	public static void rotateToAngle(int[] x,int []y,double theta,int x0,int y0) {
		  for(int i=0;i<x.length;i++) {
			int tempx = x[i];
		   x[i]=(int)((x[i]-x0)*Math.cos(theta)-(y[i]-y0)*Math.sin(theta)+x0);
		   y[i]=(int)((tempx-x0)*Math.sin(theta)+(y[i]-y0)*Math.cos(theta)+y0);
		  }

	}
}
