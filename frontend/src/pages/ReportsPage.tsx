import { useDashboardStats } from '@/hooks/useReports'
import { useOverdueTransactions } from '@/hooks/useTransactions'
import { Card, CardContent, CardHeader, CardTitle, CardDescription } from '@/components/ui/card'
import { Badge } from '@/components/ui/badge'
import { Spinner } from '@/components/ui/spinner'
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from '@/components/ui/table'
import { formatDate } from '@/lib/utils'
import { BookOpen, Users, BookCopy, AlertTriangle } from 'lucide-react'

export function ReportsPage() {
  const { data: stats, isLoading: statsLoading } = useDashboardStats()
  const { data: overdueTransactions, isLoading: overdueLoading } = useOverdueTransactions()

  return (
    <div className="space-y-6">
      <div>
        <h1 className="text-2xl font-bold text-slate-900">Reports</h1>
        <p className="text-sm text-muted-foreground mt-1">
          Library statistics and overdue book reports
        </p>
      </div>

      {statsLoading ? (
        <Spinner className="py-12" />
      ) : (
        <div className="grid gap-4 sm:grid-cols-2 lg:grid-cols-3">
          <Card>
            <CardHeader className="flex flex-row items-center justify-between pb-2 space-y-0">
              <CardTitle className="text-sm font-medium text-muted-foreground">
                Total Books
              </CardTitle>
              <BookOpen className="h-4 w-4 text-blue-600" />
            </CardHeader>
            <CardContent>
              <div className="text-2xl font-bold">{stats?.totalBooks ?? 0}</div>
              <p className="text-xs text-muted-foreground mt-1">
                {stats?.availableCopies ?? 0} of {stats?.totalCopies ?? 0} copies available
              </p>
            </CardContent>
          </Card>

          <Card>
            <CardHeader className="flex flex-row items-center justify-between pb-2 space-y-0">
              <CardTitle className="text-sm font-medium text-muted-foreground">
                Total Members
              </CardTitle>
              <Users className="h-4 w-4 text-green-600" />
            </CardHeader>
            <CardContent>
              <div className="text-2xl font-bold">{stats?.totalMembers ?? 0}</div>
              <p className="text-xs text-muted-foreground mt-1">Registered members</p>
            </CardContent>
          </Card>

          <Card>
            <CardHeader className="flex flex-row items-center justify-between pb-2 space-y-0">
              <CardTitle className="text-sm font-medium text-muted-foreground">
                Active Issues
              </CardTitle>
              <BookCopy className="h-4 w-4 text-amber-600" />
            </CardHeader>
            <CardContent>
              <div className="text-2xl font-bold">{stats?.activeIssues ?? 0}</div>
              <p className="text-xs text-muted-foreground mt-1">
                {stats?.overdueBooks ?? 0} overdue
              </p>
            </CardContent>
          </Card>
        </div>
      )}

      <Card>
        <CardHeader>
          <CardTitle className="flex items-center gap-2">
            <AlertTriangle className="h-5 w-5 text-red-500" />
            Overdue Books
          </CardTitle>
          <CardDescription>
            Books that are past their due date and need to be returned
          </CardDescription>
        </CardHeader>
        <CardContent>
          {overdueLoading ? (
            <Spinner className="py-12" />
          ) : (
            <Table>
              <TableHeader>
                <TableRow>
                  <TableHead>Book</TableHead>
                  <TableHead className="hidden sm:table-cell">Member</TableHead>
                  <TableHead className="hidden md:table-cell">Issue Date</TableHead>
                  <TableHead>Due Date</TableHead>
                  <TableHead>Status</TableHead>
                  <TableHead className="hidden sm:table-cell">Fine</TableHead>
                </TableRow>
              </TableHeader>
              <TableBody>
                {(!overdueTransactions || overdueTransactions.length === 0) ? (
                  <TableRow>
                    <TableCell colSpan={6} className="text-center py-8 text-muted-foreground">
                      No overdue books
                    </TableCell>
                  </TableRow>
                ) : (
                  overdueTransactions.map((t) => (
                    <TableRow key={t.id}>
                      <TableCell className="font-medium">{t.bookTitle}</TableCell>
                      <TableCell className="hidden sm:table-cell">{t.memberName}</TableCell>
                      <TableCell className="hidden md:table-cell text-xs">
                        {formatDate(t.issueDate)}
                      </TableCell>
                      <TableCell className="text-xs">{formatDate(t.dueDate)}</TableCell>
                      <TableCell>
                        <Badge variant="destructive">Overdue</Badge>
                      </TableCell>
                      <TableCell className="hidden sm:table-cell">
                        {t.fine > 0 ? `$${t.fine.toFixed(2)}` : '-'}
                      </TableCell>
                    </TableRow>
                  ))
                )}
              </TableBody>
            </Table>
          )}
        </CardContent>
      </Card>
    </div>
  )
}
