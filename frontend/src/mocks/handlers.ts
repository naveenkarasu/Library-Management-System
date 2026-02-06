import { http, HttpResponse, delay } from 'msw'
import { mockBooks, mockMembers, mockTransactions } from './data'
import type { Book, Member, Transaction, BookFormData, MemberFormData } from '@/types'

let books: Book[] = [...mockBooks]
let members: Member[] = [...mockMembers]
let transactions: Transaction[] = [...mockTransactions]
let nextBookId = 9
let nextMemberId = 7
let nextTransactionId = 9

export const handlers = [
  // Auth
  http.post('*/api/v1/auth/login', async ({ request }) => {
    await delay(300)
    const body = await request.json() as { username: string; password: string }
    if (body.username === 'admin' && body.password === 'admin') {
      return HttpResponse.json({
        token: 'mock-jwt-token-admin-' + Date.now(),
        username: 'admin',
        role: 'ADMIN',
      })
    }
    if (body.username === 'librarian' && body.password === 'librarian') {
      return HttpResponse.json({
        token: 'mock-jwt-token-librarian-' + Date.now(),
        username: 'librarian',
        role: 'LIBRARIAN',
      })
    }
    return HttpResponse.json(
      { message: 'Invalid username or password' },
      { status: 401 }
    )
  }),

  http.post('*/api/v1/auth/register', async () => {
    await delay(300)
    return HttpResponse.json({ message: 'User registered successfully' }, { status: 201 })
  }),

  // Books
  http.get('*/api/v1/books/search', async ({ request }) => {
    await delay(200)
    const url = new URL(request.url)
    const query = url.searchParams.get('query')?.toLowerCase() || ''
    const filtered = books.filter(
      (b) =>
        b.title.toLowerCase().includes(query) ||
        b.author.toLowerCase().includes(query) ||
        b.isbn.includes(query) ||
        b.genre.toLowerCase().includes(query)
    )
    return HttpResponse.json(filtered)
  }),

  http.get('*/api/v1/books/:id', async ({ params }) => {
    await delay(200)
    const id = Number(params.id)
    const book = books.find((b) => b.id === id)
    if (!book) {
      return HttpResponse.json({ message: 'Book not found' }, { status: 404 })
    }
    return HttpResponse.json(book)
  }),

  http.get('*/api/v1/books', async () => {
    await delay(300)
    return HttpResponse.json(books)
  }),

  http.post('*/api/v1/books', async ({ request }) => {
    await delay(300)
    const body = await request.json() as BookFormData
    const newBook: Book = {
      ...body,
      id: nextBookId++,
      createdAt: new Date().toISOString(),
      updatedAt: new Date().toISOString(),
    }
    books.push(newBook)
    return HttpResponse.json(newBook, { status: 201 })
  }),

  http.put('*/api/v1/books/:id', async ({ params, request }) => {
    await delay(300)
    const id = Number(params.id)
    const body = await request.json() as BookFormData
    const index = books.findIndex((b) => b.id === id)
    if (index === -1) {
      return HttpResponse.json({ message: 'Book not found' }, { status: 404 })
    }
    books[index] = {
      ...books[index],
      ...body,
      updatedAt: new Date().toISOString(),
    }
    return HttpResponse.json(books[index])
  }),

  http.delete('*/api/v1/books/:id', async ({ params }) => {
    await delay(300)
    const id = Number(params.id)
    books = books.filter((b) => b.id !== id)
    return new HttpResponse(null, { status: 204 })
  }),

  // Members
  http.get('*/api/v1/members/:id', async ({ params }) => {
    await delay(200)
    const id = Number(params.id)
    const member = members.find((m) => m.id === id)
    if (!member) {
      return HttpResponse.json({ message: 'Member not found' }, { status: 404 })
    }
    return HttpResponse.json(member)
  }),

  http.get('*/api/v1/members', async () => {
    await delay(300)
    return HttpResponse.json(members)
  }),

  http.post('*/api/v1/members', async ({ request }) => {
    await delay(300)
    const body = await request.json() as MemberFormData
    const newMember: Member = {
      ...body,
      id: nextMemberId++,
      membershipDate: new Date().toISOString(),
      active: true,
      createdAt: new Date().toISOString(),
      updatedAt: new Date().toISOString(),
    }
    members.push(newMember)
    return HttpResponse.json(newMember, { status: 201 })
  }),

  http.put('*/api/v1/members/:id', async ({ params, request }) => {
    await delay(300)
    const id = Number(params.id)
    const body = await request.json() as MemberFormData
    const index = members.findIndex((m) => m.id === id)
    if (index === -1) {
      return HttpResponse.json({ message: 'Member not found' }, { status: 404 })
    }
    members[index] = {
      ...members[index],
      ...body,
      updatedAt: new Date().toISOString(),
    }
    return HttpResponse.json(members[index])
  }),

  http.delete('*/api/v1/members/:id', async ({ params }) => {
    await delay(300)
    const id = Number(params.id)
    members = members.filter((m) => m.id !== id)
    return new HttpResponse(null, { status: 204 })
  }),

  // Transactions
  http.post('*/api/v1/transactions/issue', async ({ request }) => {
    await delay(400)
    const body = await request.json() as { bookId: number; memberId: number }
    const book = books.find((b) => b.id === body.bookId)
    const member = members.find((m) => m.id === body.memberId)
    if (!book || !member) {
      return HttpResponse.json({ message: 'Book or member not found' }, { status: 404 })
    }
    if (book.availableCopies <= 0) {
      return HttpResponse.json({ message: 'No copies available' }, { status: 400 })
    }
    book.availableCopies -= 1
    const now = new Date()
    const dueDate = new Date(now)
    dueDate.setDate(dueDate.getDate() + 14)
    const transaction: Transaction = {
      id: nextTransactionId++,
      bookId: body.bookId,
      memberId: body.memberId,
      bookTitle: book.title,
      memberName: member.name,
      issueDate: now.toISOString(),
      dueDate: dueDate.toISOString(),
      returnDate: null,
      status: 'ISSUED',
      fine: 0,
    }
    transactions.push(transaction)
    return HttpResponse.json(transaction, { status: 201 })
  }),

  http.post('*/api/v1/transactions/return/:id', async ({ params }) => {
    await delay(400)
    const id = Number(params.id)
    const transaction = transactions.find((t) => t.id === id)
    if (!transaction) {
      return HttpResponse.json({ message: 'Transaction not found' }, { status: 404 })
    }
    transaction.returnDate = new Date().toISOString()
    transaction.status = 'RETURNED'
    const book = books.find((b) => b.id === transaction.bookId)
    if (book) {
      book.availableCopies += 1
    }
    return HttpResponse.json(transaction)
  }),

  http.get('*/api/v1/transactions/active', async () => {
    await delay(300)
    const active = transactions.filter((t) => t.status !== 'RETURNED')
    return HttpResponse.json(active)
  }),

  http.get('*/api/v1/transactions/overdue', async () => {
    await delay(300)
    const overdue = transactions.filter((t) => t.status === 'OVERDUE')
    return HttpResponse.json(overdue)
  }),

  // Reports
  http.get('*/api/v1/reports/dashboard', async () => {
    await delay(300)
    const activeIssues = transactions.filter((t) => t.status !== 'RETURNED').length
    const overdueBooks = transactions.filter((t) => t.status === 'OVERDUE').length
    const totalCopies = books.reduce((sum, b) => sum + b.totalCopies, 0)
    const availableCopies = books.reduce((sum, b) => sum + b.availableCopies, 0)
    return HttpResponse.json({
      totalBooks: books.length,
      totalMembers: members.filter((m) => m.active).length,
      activeIssues,
      overdueBooks,
      totalCopies,
      availableCopies,
    })
  }),
]
