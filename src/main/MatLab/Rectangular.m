syms z;
f = @(z) z + 1-2*sin(pi*z);

a = 1;
b = 10;

% number of segments
N = 40;

h = (b-a)/N; % the counter

%Rectangular Method:
s=0;
for i =0:N-1
    xn= a + (i * h);
    s = s + f(xn);
end
Rectangle = h * s;