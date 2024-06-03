
function p=legendre_polinom(n)
% p=polegend(n)
% Calculeaza polinoamele Legendre de grad n
p(1,1)=1;
p(2,1:2)=[1 0];
for k=2:n
   p(k+1,1:k+1)=((2*k-1)*[p(k,1:k) 0]-(k-1)*[0 0 p(k-1,1:k-1)])/k;
end
