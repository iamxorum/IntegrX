syms z;
f = @(z) z + 1 - 2 * sin(pi * z);

a = 1;
b = 10;

% Numărul de segmente
N = 40;

h = (b - a) / N; % Lățimea fiecărui segment

% Metoda dreptunghiulară - Stânga:
s_stanga = 0;
for i = 0:N-1
    % Punctul din stânga fiecărui segment
    xn = a + (i * h);
    s_stanga = s_stanga + f(xn);
end
Rectangle_Left = h * s_stanga;

% Metoda dreptunghiulară - Dreapta:
s_dreapta = 0;
for i = 1:N
    % Punctul din dreapta fiecărui segment
    xn = a + (i * h);
    s_dreapta = s_dreapta + f(xn);
end
Rectangle_Right = h * s_dreapta;

% Metoda dreptunghiulară - Mijloc:
s_mijloc = 0;
for i = 0:N-1
    % Punctul din mijlocul fiecărui segment
    xn = a + (i * h) + (h / 2);
    s_mijloc = s_mijloc + f(xn);
end
Rectangle_Mid = h * s_mijloc;

% Afișarea rezultatelor
disp(['Metoda dreptunghiulară - Stânga: ', num2str(Rectangle_Left)]);
disp(['Metoda dreptunghiulară - Dreapta: ', num2str(Rectangle_Right)]);
disp(['Metoda dreptunghiulară - Mijloc: ', num2str(Rectangle_Mid)]);