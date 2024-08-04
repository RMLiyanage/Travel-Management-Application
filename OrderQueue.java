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
        return Integer.compare(this.priority, other.priority); // Higher priority first
    }

    @Override
    public String toString() {
        return name + "(Priority: " + priority + ", Location: " + location + ")";
    }
}

class Graph {
    private Map<String, List<Edge>> adjacencyList = new HashMap<>();

    public void addEdge(String start, String end, int weight) {
        adjacencyList.computeIfAbsent(start, k -> new ArrayList<>()).add(new Edge(end, weight));
        adjacencyList.computeIfAbsent(end, k -> new ArrayList<>()).add(new Edge(start, weight)); // Undirected graph
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

public class OrderQueue {
    private PriorityQueue<Order> fastDeliveryQueue = new PriorityQueue<>();
    private PriorityQueue<Order> normalDeliveryQueue = new PriorityQueue<>(Comparator.comparingInt(Order::getPriority));
    private Graph graph = new Graph();
    private Set<String> locations = new HashSet<>();
    private static final String DEFAULT_START_LOCATION = "A"; // Colombo

    public void enqueue(String order, boolean isFastDelivery, String location) {
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

    public void display() {
        if (fastDeliveryQueue.isEmpty() && normalDeliveryQueue.isEmpty()) {
            System.out.println("Queue is empty");
        } else {
            System.out.println("Fast delivery orders in queue: " + fastDeliveryQueue);
            System.out.println("Normal delivery orders in queue: " + normalDeliveryQueue);
        }
    }

    public void displayRoutes() {
        if (locations.isEmpty()) {
            System.out.println("No locations to display routes for.");
        } else {
            System.out.println("Displaying shortest paths for each order location starting from Colombo:");
            String currentLocation = DEFAULT_START_LOCATION;
            while (!fastDeliveryQueue.isEmpty() || !normalDeliveryQueue.isEmpty()) {
                Order nextOrder = dequeue();
                Map<String, Integer> distances = graph.dijkstra(currentLocation);
                System.out.println("Shortest path from " + currentLocation + " to " + nextOrder.getLocation() + ": " + distances.get(nextOrder.getLocation()) + " units");
                currentLocation = nextOrder.getLocation(); // Update current location to the last delivered location
            }
        }
    }

    public void addLocations(Map<String, String> districts) {
        locations.addAll(districts.keySet());
        System.out.println("Locations added: " + districts);
    }

    public void addRoute(String start, String end, int distance) {
        if (locations.contains(start) && locations.contains(end)) {
            graph.addEdge(start, end, distance);
            System.out.println("Route added: " + start + " to " + end + " with distance " + distance);
        } else {
            System.out.println("Invalid locations. Ensure both locations are added before creating a route.");
        }
    }

    public static void main(String[] args) {
        OrderQueue orderQueue = new OrderQueue();

        // Add locations (districts)
        Map<String, String> districts = new HashMap<>();
        districts.put("A", "Colombo");
        districts.put("B", "Gampaha");
        districts.put("C", "Kurunegala");
        districts.put("D", "Kandy");
        districts.put("E", "Galle");
        districts.put("F", "Matara");
        districts.put("G", "Jaffna");
        districts.put("H", "Anuradhapura");

        orderQueue.addLocations(districts);

        // Display locations
        System.out.println("Available locations:");
        for (Map.Entry<String, String> entry : districts.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }

        Scanner scanner = new Scanner(System.in);

        // Add today's available locations
        Set<String> availableLocations = new HashSet<>();
        System.out.println("Enter today's available locations (A-H) or type 'done' to finish:");
        while (true) {
            String location = scanner.nextLine();
            if (location.equalsIgnoreCase("done")) break;
            if (districts.containsKey(location)) {
                availableLocations.add(location);
                System.out.println("Location " + location + " (" + districts.get(location) + ") added to today's available locations.");
            } else {
                System.out.println("Invalid location. Please enter a valid location code (A-H).");
            }
        }

        // Add fast delivery orders
        System.out.println("Enter fast delivery orders (name, location) or type 'done' to finish:");
        while (true) {
            System.out.print("Order name: ");
            String name = scanner.nextLine();
            if (name.equalsIgnoreCase("done")) break;

            System.out.print("Order location: ");
            String location = scanner.nextLine();
            if (availableLocations.contains(location)) {
                orderQueue.enqueue(name, true, location);
            } else {
                System.out.println("Invalid location. Please enter a location from today's available locations.");
            }
        }

        // Add normal delivery orders
        System.out.println("Enter normal delivery orders (name, location) or type 'done' to finish:");
        while (true) {
            System.out.print("Order name: ");
            String name = scanner.nextLine();
            if (name.equalsIgnoreCase("done")) break;

            System.out.print("Order location: ");
            String location = scanner.nextLine();
            if (availableLocations.contains(location)) {
                orderQueue.enqueue(name, false, location);
            } else {
                System.out.println("Invalid location. Please enter a location from today's available locations.");
            }
        }

        // Add routes between locations
        orderQueue.addRoute("A", "B", 20); // Colombo to Gampaha
        orderQueue.addRoute("A", "C", 50); // Colombo to Kurunegala
        orderQueue.addRoute("B", "C", 30); // Gampaha to Kurunegala
        orderQueue.addRoute("B", "D", 40); // Gampaha to Kandy
        orderQueue.addRoute("C", "D", 70); // Kurunegala to Kandy
        orderQueue.addRoute("D", "E", 60); // Kandy to Galle
        orderQueue.addRoute("E", "F", 80); // Galle to Matara
        orderQueue.addRoute("F", "G", 120); // Matara to Jaffna
        orderQueue.addRoute("G", "H", 90); // Jaffna to Anuradhapura

        // Process orders
        orderQueue.displayRoutes();
    }
}
