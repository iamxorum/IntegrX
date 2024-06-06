
function I=gauss_legendre(f,a,b,n)
%I=gauss_legendre(f,a,b,n)
%  Calculeaza integrala functiei f pe intervalul [a,b] cu ajutorul cuadraturii Gauss-Legendre
%Legendre polin
p=legendre_polinom(n);
%Polin roots
x=roots(p(n+1,:));
% Schimbare interval de integrare de la [a,b] la [-1,1]
if a~=-1 | b~=1
   y=schimbare_QGL(f,a,b);
   G=subs(y,x);
else
   G=feval(f,x);
end
% Derivata polinomului Legendre
pn=polyder(p(n+1,:));
% Coeficienti
for i=1:n
   C(i)=2./((1-x(i).^2).*((polyval(pn,x(i))).^2));
end
% Calculul integralei cuadraturii Gauss-Legendre
I=dot(C,G);
