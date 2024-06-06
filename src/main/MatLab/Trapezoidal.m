syms x 
  
% Lower Limit 
a=0;   
  
% Upper Limit 
b=1;    
  
% Number of segments 
n=10;  
  
% Declare the function 
f= @(x) 1/(1+x^2);   
  
% h is the segment size 
h=(b - a)/n; 
  
% X stores the summation of first 
% and last segment 
X=f(a)+f(b); 
  
% variable R stores the summation of 
% all the terms from 1 to n-1 
R=0; 
for i = 1:1:n-1 
    xi=a+(i*h); 
    R=R+f(xi); 
end
  
% Formula to calculate numerical integration 
% using Trapezoidal Rule 
I=(h/2)*(X+2*R); 