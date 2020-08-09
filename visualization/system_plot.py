import matplotlib.pyplot as plt
import numpy as np

def plot_limits(L):
    plt.xlim(0, L)
    plt.ylim(0, L)
    plt.gca().set_aspect('equal', adjustable='box')

def plot_grid(L, M, color='#E6E6E6'):
    if M > 1:
        for i in range(0, M):
            horiz_x, horiz_y = [0, 20], [i*(L/M), i*(L/M)]
            vert_x, vert_y = [i*(L/M), i*(L/M)], [0, 20]
            plt.plot(horiz_x, horiz_y, vert_x, vert_y, color=color, linestyle='-')

def plot_particle(ax, x, y, r, color='#90AFAF', fill=False):
    c = plt.Circle((x,y), r, color=color, fill=fill)
    ax.add_artist(c)


def plot_out_of_limits_particle(L, ax, p, color='#90AFAF', fill=False):
    plot_particle(ax, p.position.x, p.position.y, p.ratio, color=color, fill=fill)

    bottom = upper = left = right = False

    if p.position.y - p.ratio < 0:
        bottom = True
        plot_particle(ax, p.position.x, L+p.position.y, p.ratio, color=color, fill=fill)
    if p.position.x - p.ratio < 0:
        left = True
        plot_particle(ax, L+p.position.x, p.position.y, p.ratio, color=color, fill=fill)
    if p.position.x + p.ratio > L:
        right = True
        plot_particle(ax, p.position.x-L, p.position.y, p.ratio, color=color, fill=fill)
    if p.position.y + p.ratio > L:
        upper = True
        plot_particle(ax, p.position.x, p.position.y-L, p.ratio, color=color, fill=fill)
    
    if bottom and left:
        plot_particle(ax, L+p.position.x, L+p.position.y, p.ratio, color=color, fill=fill)
    if bottom and right:
        plot_particle(ax, p.position.x-L, L+p.position.y, p.ratio, color=color, fill=fill)
    if upper and left:
        plot_particle(ax, L+p.position.x, p.position.y-L, p.ratio, color=color, fill=fill)
    if upper and right:
        plot_particle(ax, p.position.x-L, p.position.y-L, p.ratio, color=color, fill=fill)

def is_out_of_limit(L, p):
    if (p.position.x + p.ratio > L
        or p.position.x - p.ratio < 0 
        or p.position.y + p.ratio > L 
        or p.position.y - p.ratio < 0):
        return True
    return False

def plot_particles(ax, particles, color='#90AFAF', fill=False):
    for p in particles:
        plot_particle(ax, p.position.x, p.position.y, p.ratio, color=color, fill=fill)

def plot_particles_periodic_contours(L, ax, particles, color='#90AFAF', fill=False):
    for p in particles:
        if is_out_of_limit(L, p):
           plot_out_of_limits_particle(L, ax, p, color=color, fill=fill)
        else:
            circle = plt.Circle((p.position.x, p.position.y), p.ratio, color=color, fill=fill)
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
    if system.B == False: plot_particles(ax, system.particles)
    else: plot_particles_periodic_contours(L, ax, system.particles)

    plt.show()

def plot_neighbours(system, particle_number):
    L = system.L
    M = system.M

    fig, ax = plt.subplots()

    particle = system.particles[particle_number]

    plot_limits(L)

    # Plot desired particle
    if system.B == False: 
        plot_particles(ax, system.particles)
        plot_particle(ax, particle.position.x, particle.position.y, particle.ratio, color='#370516', fill=True)
    else: 
        plot_particles_periodic_contours(L, ax, system.particles)
        plot_out_of_limits_particle(L, ax, particle, color='#370516', fill=True)

    if particle.neighbours.__len__() == 0:
        plt.title("Particle {} has no neighbours".format(particle_number))
    else:   # Plot neighbours
        neighbours_qty = particle.neighbours.__len__()
        plt.title("Particle {} has {} neighbour{}".format(
            particle_number, neighbours_qty, "s" if neighbours_qty > 1 else ""))
        if system.B == False:
            for neigh in particle.neighbours:
                particle = system.particles[neigh]
                plot_particle(
                    ax, particle.position.x, particle.position.y, particle.ratio, color='#FF216E', fill=True)
        else:
            for neigh in particle.neighbours:
                p = system.particles[neigh]
                if is_out_of_limit(L, p): plot_out_of_limits_particle(L, ax, p, color='#FF216E', fill=True)
                else: plot_particle(ax, p.position.x, p.position.y, p.ratio, color='#FF216E', fill=True)

    plt.show()