/**
 * Introduction to Computer Science 2026, Ariel University,
 * Ex1: arrays, static functions and JUnit
 * https://docs.google.com/document/d/1GcNQht9rsVVSt153Y8pFPqXJVju56CY4/edit?usp=sharing&ouid=113711744349547563645&rtpof=true&sd=true
 *
 * This class represents a set of static methods on a polynomial functions - represented as an array of doubles.
 * The array {0.1, 0, -3, 0.2} represents the following polynomial function: 0.2x^3-3x^2+0.1
 * This is the main Class you should implement (see "add your code below")
 *
 * @author boaz.benmoshe

 */
public class Ex1 {
	/** Epsilon value for numerical computation, it serves as a "close enough" threshold. */
	public static final double EPS = 0.001; // the epsilon to be used for the root approximation.
	/** The zero polynomial function is represented as an array with a single (0) entry. */
	public static final double[] ZERO = {0};
	/**
	 * Computes the f(x) value of the polynomial function at x.
	 * @param poly - polynomial function
	 * @param x
	 * @return f(x) - the polynomial function value at x.
	 */
	public static double f(double[] poly, double x) {
		double ans = 0;
		for(int i=0;i<poly.length;i++) {
			double c = Math.pow(x, i);
			ans += c*poly[i];
		}
		return ans;
	}
	/** Given a polynomial function (p), a range [x1,x2] and an epsilon eps.
	 * This function computes an x value (x1<=x<=x2) for which |p(x)| < eps, 
	 * assuming p(x1)*p(x2) <= 0.
	 * This function should be implemented recursively.
	 * @param p - the polynomial function
	 * @param x1 - minimal value of the range
	 * @param x2 - maximal value of the range
	 * @param eps - epsilon (positive small value (often 10^-3, or 10^-6).
	 * @return an x value (x1<=x<=x2) for which |p(x)| < eps.
	 */
	public static double root_rec(double[] p, double x1, double x2, double eps) {
		double f1 = f(p,x1);
		double x12 = (x1+x2)/2;
		double f12 = f(p,x12);
		if (Math.abs(f12)<eps) {return x12;}
		if(f12*f1<=0) {return root_rec(p, x1, x12, eps);}
		else {return root_rec(p, x12, x2, eps);}
	}
    /**
     * Calculates the coefficients (a, b, c) of a **polynomial (parabola)** that passes through a set of up to three 2D points (x, y).
     * The function returns an array of coefficients $[c_0, c_1, \dots]$ where $c_i$ is the coefficient of $x^i$.
     * Returns **null** if the input set contains more than three points.
     *
     * Based on the solution from: http://stackoverflow.com/questions/717762/how-to-calculate-the-vertex-of-a-parabola-given-three-points
     *
     * @param xx Array of X-coordinates.
     * @param yy Array of Y-coordinates.
     * @return Array of double coefficients (e.g., $[c, b, a]$ for $ax^2+bx+c$), or null.
     */
	public static double[] PolynomFromPoints(double[] xx, double[] yy) {
        double[] ans = null;
        int lx = xx.length;
        int ly = yy.length;
        if (xx != null && yy != null && lx == ly && lx > 1 && lx < 4) {
            if (lx == 2) {
                double x0 = xx[0], x1 = xx[1];
                double y0 = yy[0], y1 = yy[1];
                if (x0 == x1) {
                    return null;
                }
                double m = (y1 - y0) / (x1 - x0); //slope
                double b =y0 - m * x0;//intercept;
                ans = new double[2];
                ans[0] = b;
                ans[1] = m;
            } else if (lx == 3) {
                double x0 = xx[0], x1 = xx[1], x2 = xx[2];
                double y0 = yy[0], y1 = yy[1], y2 = yy[2];
                double denom = (x0 - x1) * ((x0 - x2) * (x1 - x2));
                if (denom == 0) {
                    return null;
                }
                double a = (x2 * (y1 - y0) + x1 * (y0 - y2) + x0 * (y2 - y1)) / denom;
                double b = (x2 * x2 * (y0 - y1) + x1 * x1 * (y2 - y0) + x0 * x0 * (y1 - y2)) / denom;
                double c = (x1 * x2 * (x1 - x2) * y0 + x2 * x0 * (x2 - x0) * y1 + x0 * x1 * (x1 - x0) * y2) / denom;
                ans = new double[3];
                ans[0] = c;
                ans[1] = b;
                ans[2] = a;
            }
        }
        return ans;
    }





    /**
     * Determines if two polynomial functions, p1 and p2, are **functionally equal** within a tolerance **(EPS)**.
     *
     * **Principle:** Two polynomials are identical if and only if they agree at $n+1$ distinct points, where $n$ is the maximum degree of $p1$ and $p2$.
     *
     * **Implementation:** The check is performed by comparing the function values $f(x)$ for $n+1$ specific $x$ values, allowing for a small floating-point error defined by the global **EPSILON** value.
     *
     * @param p1 The first polynomial function.
     * @param p2 The second polynomial function.
     * @return true if the polynomials represent the same function, otherwise false.
     */
	public static boolean equals(double[] p1, double[] p2) {
		boolean ans = true;
      if (p1==null||p2==null) {
          return false;
      }
      int n = Math.max(p1.length -1, p2.length-1);
      for(int i=0;i<=n && ans; i++) {
          double x=i;
          double y1= f(p1,x);
          double y2= f(p2,x);
          if (Math.abs(y1-y2)> EPS) {
              ans = false;
          }

      }
		return ans;
	}

    /**
     * Compares two polynomial functions, p1 and p2, for **functional identity** using a numerical tolerance ($\epsilon$).
     *
     * **Core Principle (Identity Theorem):** Two polynomials of degree up to $n$ are identical if their values are equal at $n+1$ distinct points. Here, $n = \max(\text{degree}(p1), \text{degree}(p2))$.
     *
     * **Numerical Check:** The function evaluates $p1(x)$ and $p2(x)$ at $n+1$ chosen points. Equality is determined if $|p1(x) - p2(x)| \le \text{EPSILON}$ for all tested points, accounting for floating-point inaccuracies.
     *
     * @param p1 The first polynomial function object/representation.
     * @param p2 The second polynomial function object/representation.
     * @return True if $p1$ and $p2$ represent the same function within the specified $\epsilon$ tolerance, otherwise false.
     */
	public static String poly(double[] poly) {
		String ans = "";
		if(poly==null|| poly.length==0) {ans="0";}
		else {
         boolean firstTerm =true;
         for(int i=poly.length -1;i>=0;i--) {
             double c= poly[i];
             if (Math.abs(c)<EPS) {
                 continue;
             }
             if(firstTerm) {
                 if (c<0){
                     ans += "-";
                 }
                 firstTerm = false;
             }
             else {
                 if(c<0){
                     ans+= "-";
                 }
                 else {
                     ans += "-";
                 }
             }
             double absC= Math.abs(c);
             if (i==0){
                 ans += absC;
             }
             else {
                 ans += absC+"x";
                 if (i>1){
                     ans+= "^"+i;
                 }
             }
         }
         if (firstTerm) {
             ans="0";
         }
		}
		return ans;
	}
    /**
     * Finds a root for the difference equation $f(x) = p_1(x) - p_2(x)$, i.e., an $x$ value where $p_1(x) \approx p_2(x)$.
     *
     * **Method:** Implements the **Bisection Method** (או **Method of Halving Intervals**) within the bounded range $[x_1, x_2]$.
     *
     * **Precondition:** The function assumes the existence of a root within the interval, guaranteed by the Intermediate Value Theorem: $(p_1(x_1) - p_2(x_1)) \cdot (p_1(x_2) - p_2(x_2)) \le 0$.
     *
     * **Termination:** The iteration stops when the difference $|p_1(x) - p_2(x)|$ is less than the specified **$\text{eps}$** tolerance.
     *
     * @param p1 The first polynomial function.
     * @param p2 The second polynomial function.
     * @param x1 The lower bound of the search interval.
     * @param x2 The upper bound of the search interval.
     * @param eps The maximum acceptable absolute error between the polynomial values.
     * @return An $x \in [x_1, x_2]$ such that $|p_1(x) - p_2(x)| < \text{eps}$.
     */
	public static double sameValue(double[] p1, double[] p2, double x1, double x2, double eps) {
		double ans = x1;
        int n = Math.max(p1.length, p2.length);
        double[] diff = new double[n];
        for(int i=0;i<n;i++) {
            double c1= (i<p1.length)?p1[i]:0.0;
            double c2= (i<p2.length)?p2[i]:0.0;
            diff[i] = c1-c2;
        }
        ans= root_rec(diff,x1,x2,eps);
		return ans;
	}
    /**
     * Approximates the **arc length** of the polynomial function $p(x)$ over the closed interval $[x_1, x_2]$.
     *
     * **Methodology:** The function uses a piecewise linear approximation. The interval $[x_1, x_2]$ is divided into **$n$ segments** (where $n = \text{numberOfSegments}$). The length of the function is then approximated by the sum of the Euclidean distances between the $n+1$ sample points.
     *
     * **Implementation:** This calculation is performed **iteratively**, calculating the segment length $\sqrt{(\Delta x)^2 + (\Delta y)^2}$ for each sub-interval and accumulating the results.
     *
     * **Precondition:** Assumes $x_1 < x_2$ and $\text{numberOfSegments} \ge 1$.
     *
     * @param p The polynomial function.
     * @param x1 The start of the interval (lower bound).
     * @param x2 The end of the interval (upper bound).
     * @param numberOfSegments The number of equal sub-intervals used for the approximation.
     * @return The accumulated segment length, approximating the arc length of $p(x)$ from $x_1$ to $x_2$.
     */
	public static double length(double[] p, double x1, double x2, int numberOfSegments) {
		double ans = x1;
        if (p==null || numberOfSegments<=0|| x1==x2) {
            return 0.0;
        }
        ans=0.0;
        double dx= (x2-x1)/numberOfSegments;
        double prevX=x1;
        double prevY=f(p,prevX);
		for(int i=1;i<=numberOfSegments;i++) {
            double currX=x1+i*dx;
            double currY=f(p,currX);
            double deltax=currX-prevX;
            double deltay=currY-prevY;
            double segmentLenght= Math.sqrt(deltax*deltax+deltay*deltay);
            ans+=segmentLenght;
            prevX=currX;
            prevY=currY;
        }
        return ans;
	}

    /**
     * Approximates the **Area** between two polynomial functions, $p_1(x)$ and $p_2(x)$, over the interval $[x_1, x_2]$.
     *
     * **Methodology:** The area is calculated by approximating the definite integral of the difference function $f(x) = |p_1(x) - p_2(x)|$ using the **Trapezoidal Rule**.
     * The interval $[x_1, x_2]$ is divided into $\text{numberOfTrapezoids}$ equal sub-intervals.
     *
     * **Calculation:** The function iteratively sums the area of trapezoids formed by the difference $f(x)$ at the segment endpoints, yielding a precise approximation of the area under the difference curve.

     [Image of Trapezoidal Rule for area approximation]

     *
     * @param p1 The first polynomial function.
     * @param p2 The second polynomial function.
     * @param x1 The lower bound of the integration range.
     * @param x2 The upper bound of the integration range.
     * @param numberOfTrapezoids The number of trapezoids (sub-intervals) used for the integration approximation.
     * @return The approximated area between $p_1$ and $p_2$ in $[x_1, x_2]$.
     */
	public static double area(double[] p1,double[]p2, double x1, double x2, int numberOfTrapezoid) {
		double ans = 0;
        double dx= (x2-x1)/numberOfTrapezoid;
        double t=x1;
        for(int i=0;i<numberOfTrapezoid;i++) {
            double to=t;
            t=t+dx;
            double y1_left=f(p1,to);
            double y1_right=f(p1,t);
            double y2_left=f(p2,to);
            double y2_right=f(p2,t);
            double h_left=Math.abs(y1_left-y2_left);
            double h_right=Math.abs(y1_right-y2_right);
            double trapezoidArea=(h_left+h_right)*dx/2;
            ans+= trapezoidArea;

        }

		return ans;
	}
    /**
     * Converts a **String representation** of a polynomial function into its standard **array of coefficients** format.
     *
     * **Parsing:** The function analyzes the input string (e.g., "3x^2 - 5x + 1") to extract the numerical coefficients and their corresponding powers.
     *
     * **Inversion Test:** The function should satisfy the property that $p = \text{getPolynomFromString}(\text{poly}(p))$, where $\text{poly}(p)$ is the string representation of the coefficient array $p$.
     *
     * @param p A String representing the polynomial function (e.g., "a*x^n + ... + c").
     * @return A double array $C = [c_0, c_1, \dots, c_n]$, where $c_i$ is the coefficient of $x^i$.
     */
	public static double[] getPolynomFromString(String p) {
		double [] ans = ZERO;//  -1.0x^2 +3.0x +2.0
        if (p==null||p.trim().length()==0||p.equals("0")) {
            return ZERO;
        }
        p=p.replace(" ","");
        p=p.replace("-","+-");
        if (p.startsWith("+")) {
            p=p.substring(1);
        }
        String[] terms= p.split("\\+");
        int maxDegree=0;
        for (String term : terms) {
            if (term.contains("x^")) {
                maxDegree=Math.max(maxDegree,1);
            }
        }
        ans=new double[maxDegree+1];
        for (String term : terms) {
            if (term.length()==0) continue;
            if (term.contains("x^")) {
                String[] parts=term.split("x^");
                double coeff= (parts[0].equals("")||parts[0].equals("+"))?1:
                        (parts[0].equals("-"))?-1: Double.parseDouble(parts[0]);
            int power= Integer.parseInt(parts[1]);
            ans[power]+=coeff;
            }
            else if (term.contains("x")) {
                String coeffStr = term.replace("x","");
                double coeff= (coeffStr.equals("")||coeffStr.equals("+"))?1:
                        (coeffStr.equals("-"))?-1: Double.parseDouble(coeffStr);
                ans[1]+=coeff;
            }
            else {
                double coeff= Double.parseDouble(term);
                ans[1]+=coeff;
            }
        }
		return ans;
	}
    /**
     * Computes the coefficient array representation of the **sum** of two polynomial functions, $p_1(x)$ and $p_2(x)$.
     *
     * **Operation:** The resulting polynomial $P_{sum}$ is calculated by summing the coefficients of corresponding powers of $x$ from $p_1$ and $p_2$.
     * The degree of $P_{sum}$ will be $\max(\text{degree}(p_1), \text{degree}(p_2))$.
     *
     * **Input:** Assumes $p_1$ and $p_2$ are provided as coefficient arrays, where the index $i$ holds the coefficient of $x^i$.
     *
     * @param p1 The coefficient array of the first polynomial.
     * @param p2 The coefficient array of the second polynomial.
     * @return A new double array representing the coefficients of the sum $p_1 + p_2$.
     */
	public static double[] add(double[] p1, double[] p2) {
		double [] ans = ZERO;//
        if (p1==null&&p2==null) {
            return ZERO;
        }
        if (p1==null) {
            return p2;
        }
        if (p2==null) {
            return p1;
        }
        int n=Math.max(p1.length, p2.length);
        ans=new double[n];
        for(int i=0;i<n;i++) {
            double c1= (i<p1.length)?p1[i]:0.0;
            double c2= (i<p2.length)?p2[i]:0.0;
            ans[i]=c1+c2;
        }
		return ans;
	}
    /**
     * Calculates the **product** of two polynomials, $p_1$ and $p_2$, and returns the resulting coefficient array.
     *
     * **Mechanism:** Performs polynomial multiplication by iterating over the coefficients of the first factor and multiplying each term by every term in the second factor, then summing the results by like powers (degree).
     *
     * @param p1 The coefficient array of the multiplier.
     * @param p2 The coefficient array of the multiplicand.
     * @return The coefficient array of the polynomial $p_1 \cdot p_2$.
     */
	public static double[] mul(double[] p1, double[] p2) {
		double [] ans = ZERO;//
        if (p1==null||p2==null) {
            return ZERO;
        }
        int n=p1.length+p2.length-1;
        ans=new double[n];
        for(int i=0;i<p1.length;i++) {
            for(int j=0;j<p2.length;j++) {
                ans[i+j]+=p1[i]*p2[j];
            }
        }
		return ans;
	}
    /**
     * Computes the coefficient array representation of the **first derivative** of the polynomial function $p_0(x)$.
     *
     * **Mathematical Operation:** For a polynomial $P(x) = \sum_{i=0}^{n} c_i x^i$, its derivative is $P'(x) = \sum_{i=1}^{n} i \cdot c_i x^{i-1}$.
     *
     * **Implementation:** This is achieved by iterating through the input coefficients $c_i$ and calculating the new coefficients $c'_j = (j+1) \cdot c_{j+1}$ for the derivative $P'(x)$.
     * The degree of the resulting polynomial is $\text{degree}(p_0) - 1$.
     *
     * @param p0 The coefficient array of the input polynomial (where $p_{0}[i]$ is the coefficient of $x^i$).
     * @return A new double array representing the coefficients of the derivative $p_0'(x)$.
     */
	public static double[] derivative (double[] po) {
		double [] ans = ZERO;//
       if (po==null||po.length<=1) {
           return ZERO;
       }
       ans=new double[po.length-1];
       for(int i=1;i<po.length;i++) {
           ans[i-1]=po[i]*i;
       }
		return ans;
	}
}
