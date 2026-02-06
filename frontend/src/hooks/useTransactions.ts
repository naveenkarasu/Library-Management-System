import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query'
import { issueBook, returnBook, getActiveTransactions, getOverdueTransactions } from '@/api/transactions'
import type { IssueBookRequest } from '@/types'

export function useActiveTransactions() {
  return useQuery({
    queryKey: ['transactions', 'active'],
    queryFn: getActiveTransactions,
  })
}

export function useOverdueTransactions() {
  return useQuery({
    queryKey: ['transactions', 'overdue'],
    queryFn: getOverdueTransactions,
  })
}

export function useIssueBook() {
  const queryClient = useQueryClient()
  return useMutation({
    mutationFn: (data: IssueBookRequest) => issueBook(data),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['transactions'] })
      queryClient.invalidateQueries({ queryKey: ['books'] })
      queryClient.invalidateQueries({ queryKey: ['dashboard'] })
    },
  })
}

export function useReturnBook() {
  const queryClient = useQueryClient()
  return useMutation({
    mutationFn: (id: number) => returnBook(id),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['transactions'] })
      queryClient.invalidateQueries({ queryKey: ['books'] })
      queryClient.invalidateQueries({ queryKey: ['dashboard'] })
    },
  })
}
