function y=schimbare_QGL(f,a,b)
% Se schimba intervalul de integrare de la [a,b] la [-1,1]
syms x;
x=((b-a)./2).*x+(b+a)./2;
dx=(b-a)./2;
y=feval(f,x)*dx;