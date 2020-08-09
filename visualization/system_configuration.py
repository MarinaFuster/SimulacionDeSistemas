class Point():
    def __init__(self, x, y):
        self.x = x
        self.y = y
    
    def __str__(self):
        r = "[{}, {}]".format(self.x, self.y)
        return r

class Particle():
    def __init__(self, ratio, properties):
        self.ratio = ratio
        self.properties = properties
    
    def dynamic(self, position, velocity):
        self.position = position
        self.velocity = velocity
    
    def neighbours(self, neighbours):
        self.neighbours = neighbours
    
    def __str__(self):
        r = "Particle Description\n"
        r += "ratio: {}\n".format(self.ratio)
        r += "properties: {}\n".format(self.properties)
        if self.position or self.velocity:
            r = "Particle Dyanmic Information\n"
            if self.position: r+= "Position: {}\n".format(self.position.__str__()) 
            if self.velocity: r+= "Velocity: {}\n".format(self.velocity.__str__())
        return r
    
    def print_neighbours(self):
        if self.neighbours.__len__() > 0:
            print("Neighbours: {}".format(self.neighbours))
        else:
            print("Not neighbours at the time")

class SystemConfiguration():
    def __init__(self, static_path):
        f = open(static_path, 'r')
        lines = f.readlines()
        self.t = int(lines[0])
        self.N = int(lines[1])
        self.L = int(lines[2])
        self.M = int(lines[3])
        self.rc = int(lines[4])
        self.B = False if int(lines[5]) == 0 else True

        self.particles = []
        for i in range(6, lines.__len__()):
            particle_info = lines[i].split(',', 1)
            aux_props = particle_info[1].split(',')
            properties = []
            for prop in aux_props:
                properties.append(float(prop))
            self.particles.append(Particle(float(particle_info[0]), properties))
        f.close()

    def dynamic_configuration(self, dynamic_path):
        f = open(dynamic_path, 'r')
        lines = f.readlines()

        self.particles_time = int(lines[0])
        for i in range(1, lines.__len__()):
            coords = lines[i].split(',')
            position = Point(float(coords[0]), float(coords[1]))
            velocity = Point(float(coords[2]), float(coords[3])) if coords.__len__() > 2 else None
            self.particles[i-1].dynamic(position, velocity)
    
    def neighbours_configuration(self, neighbour_list_path):
        f = open(neighbour_list_path, 'r')
        lines = f.readlines()

        for i in range(0, lines.__len__()):
            aux_neighbours = lines[i].split(',')
            neighbours = []
            for neigh in aux_neighbours:
                neighbours.append(int(neigh))
            self.particles[i].neighbours(neighbours)

    def __str__(self):
        r = "System's static configuration\n"
        r += "t: {}\n".format(self.t)
        r += "N: {}\n".format(self.N)
        r += "L: {}\n".format(self.L)
        r += "M: {}\n".format(self.M)
        r += "rc: {}\n".format(self.rc)
        r += "B: {}\n".format(self.B)
        return r
