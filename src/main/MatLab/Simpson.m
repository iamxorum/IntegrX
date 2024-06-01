syms x 
  
% Limita inferioară
a = 4;

% Limita superioară
b = 5.2;

% Numărul de segmente
n = 6;

% Declararea funcției
f = @(x) log(x);

% h este dimensiunea segmentului
h = (b - a) / n;

% X stochează suma primului și ultimului segment
X = f(a) + f(b);

% Variabilele Odd și Even pentru a stoca
% suma termenilor impari și pari respectiv
Odd = 0;
Even = 0;

% Bucla pentru termenii impari
for i = 1:2:n-1
    xi = a + (i * h);
    Odd = Odd + f(xi);
end

% Bucla pentru termenii pari
for i = 2:2:n-2
    xi = a + (i * h);
    Even = Even + f(xi);
end

% Formula pentru a calcula integrarea numerică
% folosind regula 1/3 a lui Simpson
I = (h / 3) * (X + 4 * Odd + 2 * Even);

% Afișarea rezultatului
disp(['Integrarea folosind regula 1/3 a lui Simpson: ', num2str(I)]);
