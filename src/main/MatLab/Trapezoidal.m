syms x 
  
% Limita inferioară
a = 0;

% Limita superioară
b = 1;

% Numărul de segmente
n = 10;

% Declararea funcției
f = @(x) 1 / (1 + x^2);

% h este dimensiunea segmentului
h = (b - a) / n;

% X stochează suma primului și ultimului segment
X = f(a) + f(b);

% Variabila R stochează suma tuturor termenilor
% de la 1 la n-1
R = 0;
for i = 1:n-1
    xi = a + (i * h);
    R = R + f(xi);
end

% Formula pentru a calcula integrarea numerică
% folosind regula trapezoidală
I = (h / 2) * (X + 2 * R);

% Afișarea rezultatului
disp(['Integrarea folosind regula trapezoidală: ', num2str(I)]);
