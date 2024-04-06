syms z;
f = @(z) z + 1-2*sin(pi*z);

a = 1;  
b = 10;

% step size
h = 0.25; 

n = (b-a)/h; % the counter

%Rectangular Method:
s=0;
for i =0:n-1
    xn= a + (i * h);
    s = s + f(xn);
end
Rectangle = h * s;