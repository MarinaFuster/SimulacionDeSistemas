class Particle():

    def __init__(self, ratio, properties):
        self.ratio = ratio
        self.properties = properties
    
    def __str__(self):
        r = "Particle Description\n"
        r += "ratio: {}\n".format(self.ratio)
        r += "properties: {}\n".format(self.properties)
        return r

class SystemConfiguration():

    def __init__(self, static_path):
        with open(static_path) as f:
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

    def neighbours_configuration(self, neighbour_list_path):
        pass

    def dynamic_configuration(self, dynamic_path):
        pass

    def __str__(self):
        r = "System's static configuration\n"
        r += "t: {}\n".format(self.t)
        r += "N: {}\n".format(self.N)
        r += "L: {}\n".format(self.L)
        r += "M: {}\n".format(self.M)
        r += "rc: {}\n".format(self.rc)
        r += "B: {}".format(self.B)
        return r
