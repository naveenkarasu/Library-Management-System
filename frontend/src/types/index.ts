export interface Book {
  id: number;
  title: string;
  author: string;
  isbn: string;
  publisher: string;
  year: number;
  genre: string;
  totalCopies: number;
  availableCopies: number;
  createdAt?: string;
  updatedAt?: string;
}

export interface BookFormData {
  title: string;
  author: string;
  isbn: string;
  publisher: string;
  year: number;
  genre: string;
  totalCopies: number;
  availableCopies: number;
}

export interface Member {
  id: number;
  name: string;
  email: string;
  phone: string;
  memberType: 'STUDENT' | 'FACULTY' | 'STAFF';
  membershipDate: string;
  active: boolean;
  createdAt?: string;
  updatedAt?: string;
}

export interface MemberFormData {
  name: string;
  email: string;
  phone: string;
  memberType: 'STUDENT' | 'FACULTY' | 'STAFF';
}

export interface Transaction {
  id: number;
  bookId: number;
  memberId: number;
  bookTitle: string;
  memberName: string;
  issueDate: string;
  dueDate: string;
  returnDate: string | null;
  status: 'ISSUED' | 'RETURNED' | 'OVERDUE';
  fine: number;
}

export interface IssueBookRequest {
  bookId: number;
  memberId: number;
}

export interface User {
  username: string;
  role: 'ADMIN' | 'LIBRARIAN';
  token: string;
}

export interface LoginRequest {
  username: string;
  password: string;
}

export interface LoginResponse {
  token: string;
  username: string;
  role: 'ADMIN' | 'LIBRARIAN';
}

export interface DashboardStats {
  totalBooks: number;
  totalMembers: number;
  activeIssues: number;
  overdueBooks: number;
  totalCopies: number;
  availableCopies: number;
}

export interface ApiError {
  message: string;
  status: number;
}

export interface PaginatedResponse<T> {
  content: T[];
  totalPages: number;
  totalElements: number;
  size: number;
  number: number;
}

export interface Toast {
  id: string;
  message: string;
  type: 'success' | 'error' | 'info';
}
