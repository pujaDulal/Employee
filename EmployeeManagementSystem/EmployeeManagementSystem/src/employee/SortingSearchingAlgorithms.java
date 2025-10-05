package employee;

import java.util.*;

/**
 * Sorting and Searching Algorithms Utility Class
 * Provides efficient algorithms for managing and retrieving employee data
 */
public class SortingSearchingAlgorithms {

    /**
     * Enum for sorting criteria
     */
    public enum SortCriteria {
        ID, NAME, DEPARTMENT, SALARY, PERFORMANCE_RATING, TOTAL_SALARY, EMPLOYEE_TYPE
    }

    /**
     * Enum for sorting order
     */
    public enum SortOrder {
        ASCENDING, DESCENDING
    }

    /**
     * Quick Sort Algorithm Implementation
     * Time Complexity: O(n log n) average case, O(n²) worst case
     * Space Complexity: O(log n)
     */
    public static void quickSort(List<Employee> employees, SortCriteria criteria, SortOrder order) {
        if (employees == null || employees.size() <= 1) return;
        quickSortHelper(employees, 0, employees.size() - 1, criteria, order);
    }

    private static void quickSortHelper(List<Employee> employees, int low, int high, 
                                      SortCriteria criteria, SortOrder order) {
        if (low < high) {
            int pivotIndex = partition(employees, low, high, criteria, order);
            quickSortHelper(employees, low, pivotIndex - 1, criteria, order);
            quickSortHelper(employees, pivotIndex + 1, high, criteria, order);
        }
    }

    private static int partition(List<Employee> employees, int low, int high, 
                               SortCriteria criteria, SortOrder order) {
        Employee pivot = employees.get(high);
        int i = low - 1;

        for (int j = low; j < high; j++) {
            if (compareEmployees(employees.get(j), pivot, criteria, order) <= 0) {
                i++;
                swap(employees, i, j);
            }
        }
        swap(employees, i + 1, high);
        return i + 1;
    }

    /**
     * Merge Sort Algorithm Implementation
     * Time Complexity: O(n log n) guaranteed
     * Space Complexity: O(n)
     */
    public static void mergeSort(List<Employee> employees, SortCriteria criteria, SortOrder order) {
        if (employees == null || employees.size() <= 1) return;
        mergeSortHelper(employees, 0, employees.size() - 1, criteria, order);
    }

    private static void mergeSortHelper(List<Employee> employees, int left, int right, 
                                      SortCriteria criteria, SortOrder order) {
        if (left < right) {
            int mid = left + (right - left) / 2;
            mergeSortHelper(employees, left, mid, criteria, order);
            mergeSortHelper(employees, mid + 1, right, criteria, order);
            merge(employees, left, mid, right, criteria, order);
        }
    }

    private static void merge(List<Employee> employees, int left, int mid, int right, 
                            SortCriteria criteria, SortOrder order) {
        List<Employee> leftArray = new ArrayList<>();
        List<Employee> rightArray = new ArrayList<>();

        for (int i = left; i <= mid; i++) {
            leftArray.add(employees.get(i));
        }
        for (int i = mid + 1; i <= right; i++) {
            rightArray.add(employees.get(i));
        }

        int i = 0, j = 0, k = left;

        while (i < leftArray.size() && j < rightArray.size()) {
            if (compareEmployees(leftArray.get(i), rightArray.get(j), criteria, order) <= 0) {
                employees.set(k, leftArray.get(i));
                i++;
            } else {
                employees.set(k, rightArray.get(j));
                j++;
            }
            k++;
        }

        while (i < leftArray.size()) {
            employees.set(k, leftArray.get(i));
            i++;
            k++;
        }

        while (j < rightArray.size()) {
            employees.set(k, rightArray.get(j));
            j++;
            k++;
        }
    }

    /**
     * Heap Sort Algorithm Implementation
     * Time Complexity: O(n log n) guaranteed
     * Space Complexity: O(1)
     */
    public static void heapSort(List<Employee> employees, SortCriteria criteria, SortOrder order) {
        if (employees == null || employees.size() <= 1) return;

        int n = employees.size();

        // Build heap (rearrange array)
        for (int i = n / 2 - 1; i >= 0; i--) {
            heapify(employees, n, i, criteria, order);
        }

        // One by one extract an element from heap
        for (int i = n - 1; i > 0; i--) {
            swap(employees, 0, i);
            heapify(employees, i, 0, criteria, order);
        }
    }

    private static void heapify(List<Employee> employees, int n, int i, 
                              SortCriteria criteria, SortOrder order) {
        int largest = i;
        int left = 2 * i + 1;
        int right = 2 * i + 2;

        if (left < n && compareEmployees(employees.get(left), employees.get(largest), criteria, order) > 0) {
            largest = left;
        }

        if (right < n && compareEmployees(employees.get(right), employees.get(largest), criteria, order) > 0) {
            largest = right;
        }

        if (largest != i) {
            swap(employees, i, largest);
            heapify(employees, n, largest, criteria, order);
        }
    }

    /**
     * Insertion Sort Algorithm Implementation
     * Time Complexity: O(n²) worst case, O(n) best case
     * Space Complexity: O(1)
     * Efficient for small datasets
     */
    public static void insertionSort(List<Employee> employees, SortCriteria criteria, SortOrder order) {
        if (employees == null || employees.size() <= 1) return;

        for (int i = 1; i < employees.size(); i++) {
            Employee key = employees.get(i);
            int j = i - 1;

            while (j >= 0 && compareEmployees(employees.get(j), key, criteria, order) > 0) {
                employees.set(j + 1, employees.get(j));
                j--;
            }
            employees.set(j + 1, key);
        }
    }

    /**
     * Binary Search Algorithm Implementation
     * Time Complexity: O(log n)
     * Space Complexity: O(1)
     * Requires sorted list
     */
    public static Employee binarySearch(List<Employee> employees, String searchValue, SortCriteria criteria) {
        if (employees == null || employees.isEmpty()) return null;

        int left = 0, right = employees.size() - 1;

        while (left <= right) {
            int mid = left + (right - left) / 2;
            Employee midEmployee = employees.get(mid);
            
            String midValue = getEmployeeValue(midEmployee, criteria).toLowerCase();
            String searchLower = searchValue.toLowerCase();
            
            int comparison = midValue.compareTo(searchLower);
            
            if (comparison == 0) {
                return midEmployee;
            } else if (comparison < 0) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        return null;
    }

    /**
     * Linear Search Algorithm Implementation
     * Time Complexity: O(n)
     * Space Complexity: O(1)
     * Works with unsorted lists
     */
    public static List<Employee> linearSearch(List<Employee> employees, String searchValue, SortCriteria criteria) {
        List<Employee> results = new ArrayList<>();
        if (employees == null || employees.isEmpty()) return results;

        String searchLower = searchValue.toLowerCase();
        
        for (Employee emp : employees) {
            String empValue = getEmployeeValue(emp, criteria).toLowerCase();
            if (empValue.contains(searchLower)) {
                results.add(emp);
            }
        }
        return results;
    }

    /**
     * Advanced Search with Multiple Criteria
     * Supports partial matches and multiple search terms
     */
    public static List<Employee> advancedSearch(List<Employee> employees, Map<SortCriteria, String> searchCriteria) {
        List<Employee> results = new ArrayList<>();
        if (employees == null || employees.isEmpty()) return results;

        for (Employee emp : employees) {
            boolean matchesAll = true;
            
            for (Map.Entry<SortCriteria, String> entry : searchCriteria.entrySet()) {
                String empValue = getEmployeeValue(emp, entry.getKey()).toLowerCase();
                String searchValue = entry.getValue().toLowerCase();
                
                if (!empValue.contains(searchValue)) {
                    matchesAll = false;
                    break;
                }
            }
            
            if (matchesAll) {
                results.add(emp);
            }
        }
        return results;
    }

    /**
     * Range Search Algorithm
     * Find employees within a specific range for numeric criteria
     */
    public static List<Employee> rangeSearch(List<Employee> employees, SortCriteria criteria, 
                                          double minValue, double maxValue) {
        List<Employee> results = new ArrayList<>();
        if (employees == null || employees.isEmpty()) return results;

        for (Employee emp : employees) {
            double empValue = getEmployeeNumericValue(emp, criteria);
            if (empValue >= minValue && empValue <= maxValue) {
                results.add(emp);
            }
        }
        return results;
    }

    /**
     * Fuzzy Search Algorithm
     * Find employees with similar names using Levenshtein distance
     */
    public static List<Employee> fuzzySearch(List<Employee> employees, String searchTerm, int maxDistance) {
        List<Employee> results = new ArrayList<>();
        if (employees == null || employees.isEmpty()) return results;

        String searchLower = searchTerm.toLowerCase();
        
        for (Employee emp : employees) {
            String empName = emp.getName().toLowerCase();
            int distance = levenshteinDistance(searchLower, empName);
            
            if (distance <= maxDistance) {
                results.add(emp);
            }
        }
        return results;
    }

    /**
     * Calculate Levenshtein distance between two strings
     */
    private static int levenshteinDistance(String s1, String s2) {
        int[][] dp = new int[s1.length() + 1][s2.length() + 1];

        for (int i = 0; i <= s1.length(); i++) {
            for (int j = 0; j <= s2.length(); j++) {
                if (i == 0) {
                    dp[i][j] = j;
                } else if (j == 0) {
                    dp[i][j] = i;
                } else {
                    dp[i][j] = Math.min(
                        Math.min(dp[i - 1][j] + 1, dp[i][j - 1] + 1),
                        dp[i - 1][j - 1] + (s1.charAt(i - 1) == s2.charAt(j - 1) ? 0 : 1)
                    );
                }
            }
        }
        return dp[s1.length()][s2.length()];
    }

    /**
     * Hybrid Search Algorithm
     * Combines multiple search strategies for optimal results
     */
    public static List<Employee> hybridSearch(List<Employee> employees, String searchTerm, SortCriteria criteria) {
        List<Employee> results = new ArrayList<>();
        if (employees == null || employees.isEmpty()) return results;

        // First try exact match
        List<Employee> exactMatches = linearSearch(employees, searchTerm, criteria);
        results.addAll(exactMatches);

        // Then try fuzzy search for names
        if (criteria == SortCriteria.NAME) {
            List<Employee> fuzzyMatches = fuzzySearch(employees, searchTerm, 2);
            for (Employee emp : fuzzyMatches) {
                if (!results.contains(emp)) {
                    results.add(emp);
                }
            }
        }

        return results;
    }

    /**
     * Helper method to compare two employees based on criteria and order
     */
    private static int compareEmployees(Employee emp1, Employee emp2, SortCriteria criteria, SortOrder order) {
        int result = 0;
        
        switch (criteria) {
            case ID:
                result = emp1.getId().compareTo(emp2.getId());
                break;
            case NAME:
                result = emp1.getName().compareTo(emp2.getName());
                break;
            case DEPARTMENT:
                result = emp1.getDepartment().compareTo(emp2.getDepartment());
                break;
            case SALARY:
                result = Double.compare(emp1.getSalary(), emp2.getSalary());
                break;
            case PERFORMANCE_RATING:
                result = Integer.compare(emp1.getPerformanceRating(), emp2.getPerformanceRating());
                break;
            case TOTAL_SALARY:
                result = Double.compare(emp1.calculateSalary(), emp2.calculateSalary());
                break;
            case EMPLOYEE_TYPE:
                result = emp1.getClass().getSimpleName().compareTo(emp2.getClass().getSimpleName());
                break;
        }
        
        return order == SortOrder.DESCENDING ? -result : result;
    }

    /**
     * Helper method to get employee value based on criteria
     */
    private static String getEmployeeValue(Employee emp, SortCriteria criteria) {
        switch (criteria) {
            case ID:
                return emp.getId();
            case NAME:
                return emp.getName();
            case DEPARTMENT:
                return emp.getDepartment();
            case SALARY:
                return String.valueOf(emp.getSalary());
            case PERFORMANCE_RATING:
                return String.valueOf(emp.getPerformanceRating());
            case TOTAL_SALARY:
                return String.valueOf(emp.calculateSalary());
            case EMPLOYEE_TYPE:
                return emp.getClass().getSimpleName();
            default:
                return "";
        }
    }

    /**
     * Helper method to get employee numeric value based on criteria
     */
    private static double getEmployeeNumericValue(Employee emp, SortCriteria criteria) {
        switch (criteria) {
            case SALARY:
                return emp.getSalary();
            case PERFORMANCE_RATING:
                return emp.getPerformanceRating();
            case TOTAL_SALARY:
                return emp.calculateSalary();
            default:
                return 0.0;
        }
    }

    /**
     * Helper method to swap elements in list
     */
    private static void swap(List<Employee> employees, int i, int j) {
        Employee temp = employees.get(i);
        employees.set(i, employees.get(j));
        employees.set(j, temp);
    }

    /**
     * Get sorting algorithm performance metrics
     */
    public static class SortingMetrics {
        private final long executionTime;
        private final int comparisons;
        private final int swaps;
        private final String algorithmName;

        public SortingMetrics(String algorithmName, long executionTime, int comparisons, int swaps) {
            this.algorithmName = algorithmName;
            this.executionTime = executionTime;
            this.comparisons = comparisons;
            this.swaps = swaps;
        }

        public long getExecutionTime() { return executionTime; }
        public int getComparisons() { return comparisons; }
        public int getSwaps() { return swaps; }
        public String getAlgorithmName() { return algorithmName; }

        @Override
        public String toString() {
            return String.format("%s: %dms, %d comparisons, %d swaps", 
                               algorithmName, executionTime, comparisons, swaps);
        }
    }

    /**
     * Sort with performance tracking
     */
    public static SortingMetrics sortWithMetrics(List<Employee> employees, SortCriteria criteria, 
                                               SortOrder order, String algorithmName) {
        long startTime = System.currentTimeMillis();
        
        switch (algorithmName.toLowerCase()) {
            case "quicksort":
                quickSort(employees, criteria, order);
                break;
            case "mergesort":
                mergeSort(employees, criteria, order);
                break;
            case "heapsort":
                heapSort(employees, criteria, order);
                break;
            case "insertionsort":
                insertionSort(employees, criteria, order);
                break;
            default:
                quickSort(employees, criteria, order);
                algorithmName = "quicksort";
        }
        
        long endTime = System.currentTimeMillis();
        return new SortingMetrics(algorithmName, endTime - startTime, 0, 0);
    }
}
