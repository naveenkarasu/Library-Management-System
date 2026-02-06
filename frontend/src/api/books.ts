import apiClient from './client'
import type { Book, BookFormData } from '@/types'

export async function getBooks(): Promise<Book[]> {
  const response = await apiClient.get<Book[]>('/api/v1/books')
  return response.data
}

export async function getBook(id: number): Promise<Book> {
  const response = await apiClient.get<Book>(`/api/v1/books/${id}`)
  return response.data
}

export async function createBook(data: BookFormData): Promise<Book> {
  const response = await apiClient.post<Book>('/api/v1/books', data)
  return response.data
}

export async function updateBook(id: number, data: BookFormData): Promise<Book> {
  const response = await apiClient.put<Book>(`/api/v1/books/${id}`, data)
  return response.data
}

export async function deleteBook(id: number): Promise<void> {
  await apiClient.delete(`/api/v1/books/${id}`)
}

export async function searchBooks(query: string): Promise<Book[]> {
  const response = await apiClient.get<Book[]>('/api/v1/books/search', {
    params: { query },
  })
  return response.data
}
