import java.util.*;

class Order implements Comparable<Order> {
    private String name;
    private int priority;
    private String location;

    public Order(String name, int priority, String location) {
        this.name = name;
        this.priority = priority;
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public int getPriority() {
        return priority;
    }

    public String getLocation() {
        return location;
    }

    @Override
    public int compareTo(Order other) {
        return Integer.compare(other.priority, this.priority); 
    }

    @Override
    public String toString() {
        return name + " (Priority: " + priority + ", Location: " + location + ")";
    }
}

class Graph {
    private Map<String, List<Edge>> adjacencyList = new HashMap<>();

    public void addEdge(String start, String end, int weight) {
        adjacencyList.computeIfAbsent(start, k -> new ArrayList<>()).add(new Edge(end, weight));
        adjacencyList.computeIfAbsent(end, k -> new ArrayList<>()).add(new Edge(start, weight)); 
    }

    public Map<String, Integer> dijkstra(String start) {
        Map<String, Integer> distances = new HashMap<>();
        PriorityQueue<Edge> priorityQueue = new PriorityQueue<>(Comparator.comparingInt(e -> e.weight));

        for (String vertex : adjacencyList.keySet()) {
            distances.put(vertex, Integer.MAX_VALUE);
        }
        distances.put(start, 0);
        priorityQueue.add(new Edge(start, 0));

        while (!priorityQueue.isEmpty()) {
            Edge currentEdge = priorityQueue.poll();
            String currentNode = currentEdge.destination;

            if (adjacencyList.get(currentNode) == null) continue;

            for (Edge edge : adjacencyList.get(currentNode)) {
                int newDist = distances.get(currentNode) + edge.weight;
                if (newDist < distances.get(edge.destination)) {
                    distances.put(edge.destination, newDist);
                    priorityQueue.add(new Edge(edge.destination, newDist));
                }
            }
        }
        return distances;
    }
}

class Edge {
    String destination;
    int weight;

    public Edge(String destination, int weight) {
        this.destination = destination;
        this.weight = weight;
    }

    @Override
    public String toString() {
        return destination + " (" + weight + ")";
    }
}

public class OrderQueue1 {
    private PriorityQueue<Order> fastDeliveryQueue = new PriorityQueue<>();
    private PriorityQueue<Order> normalDeliveryQueue = new PriorityQueue<>(Comparator.comparingInt(Order::getPriority));
    private Graph graph = new Graph();
    private Set<String> availableLocations = new HashSet<>();
    private static final String DEFAULT_START_LOCATION = "A"; 

    // Existing available locations (for reference)
    private final Map<String, String> allLocations = Map.of(
        "A", "Colombo",
        "B", "Gampaha",
        "C", "Kurunegala",
        "D", "Kandy",
        "E", "Galle",
        "F", "Matara",
        "G", "Jaffna",
        "H", "Anuradhapura"
    );

    public void inputAvailableLocations() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Select today's available locations (enter the letter corresponding to each location):");
        for (Map.Entry<String, String> entry : allLocations.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
        System.out.println("Enter the letters of available locations (e.g., ABC for Colombo, Gampaha, and Kurunegala):");
        String input = scanner.nextLine().toUpperCase();

        availableLocations.clear(); 
        for (char c : input.toCharArray()) {
            String locationKey = String.valueOf(c);
            if (allLocations.containsKey(locationKey)) {
                availableLocations.add(locationKey);
            }
        }

        System.out.println("Available locations for today: " + availableLocations);
    }

    public void enqueue(String order, boolean isFastDelivery, String location) {
        if (!availableLocations.contains(location)) {
            System.out.println("Invalid location. The location is not available for today.");
            return;
        }
        int priority = isFastDelivery ? 1 : 2;
        Order newOrder = new Order(order, priority, location);
        if (isFastDelivery) {
            fastDeliveryQueue.add(newOrder);
        } else {
            normalDeliveryQueue.add(newOrder);
        }
        System.out.println("Order added: " + order + " with priority: " + priority + ", location: " + location);
    }

    public Order dequeue() {
        if (!fastDeliveryQueue.isEmpty()) {
            Order order = fastDeliveryQueue.poll();
            System.out.println("Fast delivery order processed: " + order);
            return order;
        } else if (!normalDeliveryQueue.isEmpty()) {
            Order order = normalDeliveryQueue.poll();
            System.out.println("Normal delivery order processed: " + order);
            return order;
        } else {
            System.out.println("Queue is empty");
            return null;
        }
    }

    public void showAvailableLocations() {
        System.out.println("Available Locations:");
        for (String location : availableLocations) {
            System.out.println(location + ": " + allLocations.get(location));
        }
    }

    public void showFastDeliveryOrders() {
        System.out.println("Today's available fast delivery orders:");
        if (fastDeliveryQueue.isEmpty()) {
            System.out.println("No fast delivery orders available.");
        } else {
            for (Order order : fastDeliveryQueue) {
                System.out.println(order);
            }
        }
    }

    public void showNormalDeliveryOrders() {
        System.out.println("Today's available normal delivery orders:");
        if (normalDeliveryQueue.isEmpty()) {
            System.out.println("No normal delivery orders available.");
        } else {
            for (Order order : normalDeliveryQueue) {
                System.out.println(order);
            }
        }
    }

    public void displayRoutes() {
        if (availableLocations.isEmpty()) {
            System.out.println("No locations to display routes for.");
        } else {
            System.out.println("Displaying shortest paths for each order location starting from Colombo:");
            String currentLocation = DEFAULT_START_LOCATION;
            while (!fastDeliveryQueue.isEmpty() || !normalDeliveryQueue.isEmpty()) {
                Order nextOrder = dequeue();
                if (nextOrder == null) break; 

                Map<String, Integer> distances = graph.dijkstra(currentLocation);
                Integer distance = distances.get(nextOrder.getLocation());
                if (distance != null) {
                    System.out.println("Shortest path from " + currentLocation + " to " + nextOrder.getLocation() + ": " + distance + " units");
                } else {
                    System.out.println("No path found from " + currentLocation + " to " + nextOrder.getLocation());
                }
                currentLocation = nextOrder.getLocation(); 
            }
        }
    }

    public boolean isLocationValid(String location) {
        return availableLocations.contains(location);
    }

    public void addLocations() {
        availableLocations.addAll(allLocations.keySet());
    }

    public void addRoute(String start, String end, int distance) {
        if (allLocations.containsKey(start) && allLocations.containsKey(end)) {
            graph.addEdge(start, end, distance);
        } else {
            System.out.println("Invalid locations. Ensure both locations are added before creating a route.");
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        OrderQueue1 orderQueue = new OrderQueue1();

         
        orderQueue.addRoute("A", "B", 20);
        orderQueue.addRoute("A", "C", 50);
        orderQueue.addRoute("A", "D", 100);
        orderQueue.addRoute("A", "E", 150);
        orderQueue.addRoute("A", "F", 200);
        orderQueue.addRoute("A", "G", 250);
        orderQueue.addRoute("A", "H", 300);

        orderQueue.addRoute("B", "C", 30);
        orderQueue.addRoute("B", "D", 60);
        orderQueue.addRoute("B", "E", 90);
        orderQueue.addRoute("B", "F", 130);
        orderQueue.addRoute("B", "G", 180);
        orderQueue.addRoute("B", "H", 230);

        orderQueue.addRoute("C", "D", 70);
        orderQueue.addRoute("C", "E", 100);
        orderQueue.addRoute("C", "F", 140);
        orderQueue.addRoute("C", "G", 190);
        orderQueue.addRoute("C", "H", 240);

        orderQueue.addRoute("D", "E", 60);
        orderQueue.addRoute("D", "F", 100);
        orderQueue.addRoute("D", "G", 150);
        orderQueue.addRoute("D", "H", 200);

        orderQueue.addRoute("E", "F", 80);
        orderQueue.addRoute("E", "G", 130);
        orderQueue.addRoute("E", "H", 180);

        orderQueue.addRoute("F", "G", 120);
        orderQueue.addRoute("F", "H", 170);

        orderQueue.addRoute("G", "H", 90);

        while (true) {
            orderQueue.inputAvailableLocations();

            while (true) {
                System.out.println("\n1. Add Fast Delivery Orders");
                System.out.println("2. Add Normal Delivery Orders");
                System.out.println("3. Show Available Locations");
                System.out.println("4. Show Fast Delivery Orders");
                System.out.println("5. Show Normal Delivery Orders");
                System.out.println("6. Display Routes");
                System.out.println("7. Exit");
                System.out.println("8. Restart Program");
                System.out.print("Enter your choice: ");
                int choice = scanner.nextInt();
                scanner.nextLine(); 

                switch (choice) {
                    case 1:
                        while (true) {
                            System.out.print("Enter fast delivery order name (or type 'done' to finish): ");
                            String fastOrder = scanner.nextLine();
                            if (fastOrder.equalsIgnoreCase("done")) break;
                            System.out.print("Enter delivery location (A-H): ");
                            String fastLocation = scanner.nextLine().toUpperCase();
                            if (!orderQueue.isLocationValid(fastLocation)) {
                                System.out.println("Invalid location. Please enter a valid location (A-H).");
                                continue;
                            }
                            orderQueue.enqueue(fastOrder, true, fastLocation);
                        }
                        break;

                    case 2:
                        while (true) {
                            System.out.print("Enter normal delivery order name (or type 'done' to finish): ");
                            String normalOrder = scanner.nextLine();
                            if (normalOrder.equalsIgnoreCase("done")) break;
                            System.out.print("Enter delivery location (A-H): ");
                            String normalLocation = scanner.nextLine().toUpperCase();
                            if (!orderQueue.isLocationValid(normalLocation)) {
                                System.out.println("Invalid location. Please enter a valid location (A-H).");
                                continue;
                            }
                            orderQueue.enqueue(normalOrder, false, normalLocation);
                        }
                        break;

                    case 3:
                        orderQueue.showAvailableLocations();
                        break;

                    case 4:
                        orderQueue.showFastDeliveryOrders();
                        break;

                    case 5:
                        orderQueue.showNormalDeliveryOrders();
                        break;

                    case 6:
                        orderQueue.displayRoutes();
                        break;

                    case 7:
                        System.out.println("Exiting...");
                        return;

                    case 8:
                        System.out.println("Restarting the program...");
                        break;

                    default:
                        System.out.println("Invalid choice. Please try again.");
                }

                if (choice == 8) {
                    break; 
                }
            }
        }
    }
}
