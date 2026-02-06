import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card'
import { Spinner } from '@/components/ui/spinner'
import { BookShelf3D } from '@/components/three/BookShelf3D'
import { useDashboardStats } from '@/hooks/useReports'
import { useBooks } from '@/hooks/useBooks'
import { BookOpen, Users, BookPlus, AlertTriangle } from 'lucide-react'

export function Dashboard() {
  const { data: stats, isLoading: statsLoading } = useDashboardStats()
  const { data: books, isLoading: booksLoading } = useBooks()

  const statCards = [
    {
      title: 'Total Books',
      value: stats?.totalBooks ?? 0,
      icon: BookOpen,
      color: 'text-blue-600',
      bg: 'bg-blue-50',
    },
    {
      title: 'Total Members',
      value: stats?.totalMembers ?? 0,
      icon: Users,
      color: 'text-green-600',
      bg: 'bg-green-50',
    },
    {
      title: 'Active Issues',
      value: stats?.activeIssues ?? 0,
      icon: BookPlus,
      color: 'text-amber-600',
      bg: 'bg-amber-50',
    },
    {
      title: 'Overdue Books',
      value: stats?.overdueBooks ?? 0,
      icon: AlertTriangle,
      color: 'text-red-600',
      bg: 'bg-red-50',
    },
  ]

  return (
    <div className="space-y-6">
      <div>
        <h1 className="text-2xl font-bold text-slate-900">Dashboard</h1>
        <p className="text-sm text-muted-foreground mt-1">
          Overview of library statistics and book inventory
        </p>
      </div>

      {statsLoading ? (
        <Spinner className="py-12" />
      ) : (
        <div className="grid gap-4 sm:grid-cols-2 lg:grid-cols-4">
          {statCards.map((card) => (
            <Card key={card.title}>
              <CardHeader className="flex flex-row items-center justify-between pb-2 space-y-0">
                <CardTitle className="text-sm font-medium text-muted-foreground">
                  {card.title}
                </CardTitle>
                <div className={`p-2 rounded-lg ${card.bg}`}>
                  <card.icon className={`h-4 w-4 ${card.color}`} />
                </div>
              </CardHeader>
              <CardContent>
                <div className="text-2xl font-bold">{card.value}</div>
              </CardContent>
            </Card>
          ))}
        </div>
      )}

      <Card>
        <CardHeader>
          <CardTitle>Book Shelf</CardTitle>
        </CardHeader>
        <CardContent>
          {booksLoading ? (
            <Spinner className="py-12" />
          ) : (
            <BookShelf3D books={books ?? []} />
          )}
        </CardContent>
      </Card>
    </div>
  )
}
