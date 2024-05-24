% Initial conditions
x0 = 0;
y0 = 0;
h = 0.5;
x_end = 10;

% Time-stepping loop
x_values = x0;
y_values = y0;
x = x0;
y = y0;

while x < x_end
    y = rk4_step(@f, x, y, h);
    x = x + h;
    x_values = [x_values, x];
    y_values = [y_values, y];
end

% Plot the solution
figure;
plot(x_values, y_values);
xlabel('x');
ylabel('y');
title('Solution of dy/dx = cos(x) using RK4');
grid on;
hold on;

% Fill the area under the curve
x_fill = [x_values, fliplr(x_values)];
y_fill = [y_values, zeros(size(y_values))];
fill(x_fill, y_fill, 'cyan', 'FaceAlpha', 0.3);
hold off;

% Print the final value of the integral
fprintf('The integral of cos(x) from 0 to 10 is approximately: %.6f\n', y_values(end));

% Enable data cursor
datacursormode on;

% Save the figure as .fig file
savefig('solution_figure.fig');

% Define the Runge-Kutta 4th order step function
function y = rk4_step(f, x, y, h)
    k1 = h * f(x, y);
    k2 = h * f(x + h / 2, y + k1 / 2);
    k3 = h * f(x + h / 2, y + k2 / 2);
    k4 = h * f(x + h, y + k3);
    y = y + (k1 + 2 * k2 + 2 * k3 + k4) / 6;
end

% Define the ODE dy/dx = cos(x)
function dydx = f(x, y)
    dydx = x.^3 - 2*x.^2 + 3*x - 4;
end