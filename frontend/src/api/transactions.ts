import apiClient from './client'
import type { Transaction, IssueBookRequest } from '@/types'

export async function issueBook(data: IssueBookRequest): Promise<Transaction> {
  const response = await apiClient.post<Transaction>('/api/v1/transactions/issue', data)
  return response.data
}

export async function returnBook(id: number): Promise<Transaction> {
  const response = await apiClient.post<Transaction>(`/api/v1/transactions/return/${id}`)
  return response.data
}

export async function getActiveTransactions(): Promise<Transaction[]> {
  const response = await apiClient.get<Transaction[]>('/api/v1/transactions/active')
  return response.data
}

export async function getOverdueTransactions(): Promise<Transaction[]> {
  const response = await apiClient.get<Transaction[]>('/api/v1/transactions/overdue')
  return response.data
}
