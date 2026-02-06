import { useState } from 'react'
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from '@/components/ui/table'
import { Button } from '@/components/ui/button'
import { Badge } from '@/components/ui/badge'
import { Pencil, Trash2, ChevronLeft, ChevronRight } from 'lucide-react'
import type { Book } from '@/types'

interface BookTableProps {
  books: Book[];
  onEdit: (book: Book) => void;
  onDelete: (book: Book) => void;
}

const PAGE_SIZE = 8

export function BookTable({ books, onEdit, onDelete }: BookTableProps) {
  const [page, setPage] = useState(0)
  const totalPages = Math.ceil(books.length / PAGE_SIZE)
  const paginatedBooks = books.slice(page * PAGE_SIZE, (page + 1) * PAGE_SIZE)

  const getAvailabilityBadge = (book: Book) => {
    if (book.availableCopies === 0) {
      return <Badge variant="destructive">Unavailable</Badge>
    }
    if (book.availableCopies <= 1) {
      return <Badge variant="warning">Low Stock</Badge>
    }
    return <Badge variant="success">Available</Badge>
  }

  return (
    <div>
      <Table>
        <TableHeader>
          <TableRow>
            <TableHead>Title</TableHead>
            <TableHead className="hidden md:table-cell">Author</TableHead>
            <TableHead className="hidden lg:table-cell">ISBN</TableHead>
            <TableHead className="hidden sm:table-cell">Genre</TableHead>
            <TableHead>Copies</TableHead>
            <TableHead>Status</TableHead>
            <TableHead className="text-right">Actions</TableHead>
          </TableRow>
        </TableHeader>
        <TableBody>
          {paginatedBooks.length === 0 ? (
            <TableRow>
              <TableCell colSpan={7} className="text-center py-8 text-muted-foreground">
                No books found
              </TableCell>
            </TableRow>
          ) : (
            paginatedBooks.map((book) => (
              <TableRow key={book.id}>
                <TableCell className="font-medium">{book.title}</TableCell>
                <TableCell className="hidden md:table-cell">{book.author}</TableCell>
                <TableCell className="hidden lg:table-cell text-xs text-muted-foreground">
                  {book.isbn}
                </TableCell>
                <TableCell className="hidden sm:table-cell">{book.genre}</TableCell>
                <TableCell>
                  {book.availableCopies}/{book.totalCopies}
                </TableCell>
                <TableCell>{getAvailabilityBadge(book)}</TableCell>
                <TableCell className="text-right">
                  <div className="flex justify-end gap-1">
                    <Button variant="ghost" size="icon" onClick={() => onEdit(book)}>
                      <Pencil className="h-4 w-4" />
                    </Button>
                    <Button
                      variant="ghost"
                      size="icon"
                      onClick={() => onDelete(book)}
                      className="text-destructive"
                    >
                      <Trash2 className="h-4 w-4" />
                    </Button>
                  </div>
                </TableCell>
              </TableRow>
            ))
          )}
        </TableBody>
      </Table>

      {totalPages > 1 && (
        <div className="flex items-center justify-between mt-4 px-2">
          <p className="text-sm text-muted-foreground">
            Showing {page * PAGE_SIZE + 1}-{Math.min((page + 1) * PAGE_SIZE, books.length)} of{' '}
            {books.length} books
          </p>
          <div className="flex items-center gap-2">
            <Button
              variant="outline"
              size="sm"
              disabled={page === 0}
              onClick={() => setPage((p) => p - 1)}
            >
              <ChevronLeft className="h-4 w-4" />
            </Button>
            <span className="text-sm text-muted-foreground">
              Page {page + 1} of {totalPages}
            </span>
            <Button
              variant="outline"
              size="sm"
              disabled={page >= totalPages - 1}
              onClick={() => setPage((p) => p + 1)}
            >
              <ChevronRight className="h-4 w-4" />
            </Button>
          </div>
        </div>
      )}
    </div>
  )
}
