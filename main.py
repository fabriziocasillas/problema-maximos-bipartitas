#fabrizio pimentel casillas
#arvin isaac marin gallegos
class Nodo:
    def __init__(self, valor):
        self.valor = valor
        self.vecinos = {}

    def conectar(self, otro, peso=1):
        self.vecinos[otro] = peso

    def mostrar_conexiones(self):
        print(f"\nNodo {self.valor}")
        
        if not self.vecinos:
            print("  Sin conexiones")
            return

        for vecino, peso in self.vecinos.items():
            print(f"  -> {vecino.valor} (peso {peso})")


def main():
    a = Nodo("A")
    b = Nodo("B")

    a.conectar(b, 5)



    a.mostrar_conexiones()
    b.mostrar_conexiones()



if __name__ == "__main__":
    main()