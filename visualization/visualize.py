import json
import matplotlib.pyplot as plt
import tkinter as tk
import os
from tkinter import *
from tkinter import ttk, font
from system_plot import plot_space, plot_particles_in_space, plot_neighbours
from system_configuration import SystemConfiguration

current_path = os.path.dirname(os.path.realpath(__file__))

class Application():
    def __init__(self, system):
        self.system = system
        self.root = tk.Tk()
        self.root.geometry("500x200")
        self.root.title("Particles and Neighbours' Visualization")

        self.space = ttk.Button(self.root, text="Plot Space", command=self.space)
        self.particles = ttk.Button(self.root, text="Plot Particles", command=self.particles)
        self.neighbours = ttk.Button(self.root, text="Plot Neighbours", command=self.neighbours)

        self.space.pack(side=LEFT, fill=BOTH, expand=True, padx=5, pady=5)
        self.particles.pack(side=RIGHT, fill=BOTH, expand=True, padx=5, pady=5)
        self.neighbours.pack(side=RIGHT, fill=BOTH, expand=True, padx=5, pady=5)
        
        self.root.mainloop()
    
    def accept(self):
        plot_neighbours(self.system, self.particle.get())
    
    def remove(self):
        self.tag.destroy()
        self.particle_box.destroy()
        self.accept.destroy()
        self.cancel.destroy()

    def space(self):
        plot_space(self.system)

    def particles(self):
        plot_particles_in_space(self.system)
    
    def neighbours(self):
        # Tag for particle input
        self.tag = ttk.Label(self.root, text="Particle Number:", font=font.Font(weight='bold'))
        self.particle = IntVar()

        # Box for user's input
        self.particle_box = ttk.Entry(self.root, textvariable=self.particle, width=30)
        
        # Buttons to accept and cancel
        self.accept = ttk.Button(self.root, text="Accept", command=self.accept)
        self.cancel = ttk.Button(self.root, text="Cancel", command=self.remove)

        # Set positions for elements
        self.tag.pack(side=TOP, fill=BOTH, expand=True, 
                        padx=5, pady=5)
        self.particle_box.pack(side=TOP, fill=X, expand=True, 
                         padx=5, pady=5)
        self.accept.pack(side=LEFT, fill=BOTH, expand=True, 
                         padx=5, pady=5)
        self.cancel.pack(side=RIGHT, fill=BOTH, expand=True, 
                         padx=5, pady=5)
        
        self.particle_box.focus_set()

def main():
    with open(current_path + "/config.json") as f:
        config = json.load(f)
        system = SystemConfiguration(config)
        app = Application(system)

if __name__ == '__main__':
    main()