import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query'
import { getBooks, getBook, createBook, updateBook, deleteBook, searchBooks } from '@/api/books'
import type { BookFormData } from '@/types'

export function useBooks() {
  return useQuery({
    queryKey: ['books'],
    queryFn: getBooks,
  })
}

export function useBook(id: number) {
  return useQuery({
    queryKey: ['books', id],
    queryFn: () => getBook(id),
    enabled: id > 0,
  })
}

export function useSearchBooks(query: string) {
  return useQuery({
    queryKey: ['books', 'search', query],
    queryFn: () => searchBooks(query),
    enabled: query.length > 0,
  })
}

export function useCreateBook() {
  const queryClient = useQueryClient()
  return useMutation({
    mutationFn: (data: BookFormData) => createBook(data),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['books'] })
      queryClient.invalidateQueries({ queryKey: ['dashboard'] })
    },
  })
}

export function useUpdateBook() {
  const queryClient = useQueryClient()
  return useMutation({
    mutationFn: ({ id, data }: { id: number; data: BookFormData }) => updateBook(id, data),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['books'] })
      queryClient.invalidateQueries({ queryKey: ['dashboard'] })
    },
  })
}

export function useDeleteBook() {
  const queryClient = useQueryClient()
  return useMutation({
    mutationFn: (id: number) => deleteBook(id),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['books'] })
      queryClient.invalidateQueries({ queryKey: ['dashboard'] })
    },
  })
}
