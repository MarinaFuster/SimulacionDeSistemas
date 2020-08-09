import matplotlib.pyplot as plt
import numpy as np

def plot_limits(L):
    plt.xlim(0, L)
    plt.ylim(0, L)
    plt.gca().set_aspect('equal', adjustable='box')

def plot_grid(L, M):
    if M > 1:
        for i in range(0, M):
            horiz_x, horiz_y = [0, 20], [i*(L/M), i*(L/M)]
            vert_x, vert_y = [i*(L/M), i*(L/M)], [0, 20]
            plt.plot(horiz_x, horiz_y, vert_x, vert_y, color='#E6E6E6', linestyle='-')

def plot_particles(ax, particles):
    for particle in particles:
        circle = plt.Circle(
            (particle.position.x, particle.position.y), particle.ratio, color='#90AFAF', fill=False)
        ax.add_artist(circle)


# Plots space of the system where the particles will be
def plot_space(system, grid=True):
    L = system.L
    M = system.M

    fig, ax = plt.subplots()

    plot_limits(L)
    if grid: plot_grid(L, M)

    plt.suptitle("System's Space\nSpace area: {}, Cell quantity: {}".format(L*L, M*M))
    plt.show()

def plot_particles_in_space(system, grid=False):
    L = system.L
    M = system.M

    fig, ax = plt.subplots()

    plot_limits(L)
    plot_particles(ax, system.particles)
    # if grid: plot_grid(L, M)

    plt.show()

def plot_neighbours(system, particle):
    L = system.L
    M = system.M

    fig, ax = plt.subplots()

    plot_limits(L)
    plot_particles(ax, system.particles)
    
    circle = plt.Circle((particle.position.x, particle.position.y), particle.ratio, color='#370516', fill=True)
    ax.add_artist(circle)
    for neigh in particle.neighbours:
        particle = system.particles[neigh]
        c = plt.Circle((particle.position.x, particle.position.y), particle.ratio, color='#FF216E', fill=True)
        ax.add_artist(c)

    plt.show()