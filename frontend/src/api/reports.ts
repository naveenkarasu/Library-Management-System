import apiClient from './client'
import type { DashboardStats } from '@/types'

export async function getDashboardStats(): Promise<DashboardStats> {
  const response = await apiClient.get<DashboardStats>('/api/v1/reports/dashboard')
  return response.data
}
