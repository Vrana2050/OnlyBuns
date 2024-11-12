export interface Page<T> {
    content: T[];          // The actual content (list of users in this case)
    totalElements: number; // Total number of items
    totalPages: number;    // Total number of pages
    size: number;          // Size of each page
    number: number;        // Current page number (0-based index)
  }
  