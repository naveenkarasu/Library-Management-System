import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card'
import { Spinner } from '@/components/ui/spinner'
import { BookShelf3D } from '@/components/three/BookShelf3D'
import { StatsRing3D } from '@/components/three/StatsRing3D'
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
      color: 'text-teal-600',
      bg: 'bg-teal-50',
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
      color: 'text-gold-600',
      bg: 'bg-gold-50',
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
        <h1 className="text-2xl font-bold text-stone-900">Dashboard</h1>
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

      {stats && (
        <Card>
          <CardHeader>
            <CardTitle>Library Overview</CardTitle>
          </CardHeader>
          <CardContent>
            <StatsRing3D stats={{
              totalBooks: stats.totalBooks ?? 0,
              totalMembers: stats.totalMembers ?? 0,
              activeIssues: stats.activeIssues ?? 0,
              overdueBooks: stats.overdueBooks ?? 0,
            }} />
            <div className="flex justify-center gap-6 mt-4 text-xs">
              <span className="flex items-center gap-1"><span className="w-3 h-3 rounded-full bg-teal-500" /> Books</span>
              <span className="flex items-center gap-1"><span className="w-3 h-3 rounded-full bg-green-500" /> Members</span>
              <span className="flex items-center gap-1"><span className="w-3 h-3 rounded-full bg-amber-500" /> Issues</span>
              <span className="flex items-center gap-1"><span className="w-3 h-3 rounded-full bg-red-500" /> Overdue</span>
            </div>
          </CardContent>
        </Card>
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
