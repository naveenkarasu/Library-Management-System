import { useQuery } from '@tanstack/react-query'
import { getDashboardStats } from '@/api/reports'

export function useDashboardStats() {
  return useQuery({
    queryKey: ['dashboard'],
    queryFn: getDashboardStats,
  })
}
