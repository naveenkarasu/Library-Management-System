import { Suspense, useState, useRef } from 'react'
import { Canvas } from '@react-three/fiber'
import { OrbitControls, Html, RoundedBox } from '@react-three/drei'
import type { Book } from '@/types'
import * as THREE from 'three'

interface BookShelf3DProps {
  books: Book[];
  onBookClick?: (book: Book) => void;
}

function getBookColor(book: Book): string {
  if (book.availableCopies === 0) return '#ef4444' // red - all checked out
  if (book.availableCopies <= 1) return '#eab308' // yellow - low stock
  return '#22c55e' // green - available
}

const BOOK_COLORS = [
  '#4f46e5', '#7c3aed', '#2563eb', '#0891b2', '#059669',
  '#d97706', '#dc2626', '#be185d', '#6366f1', '#8b5cf6',
]

function BookMesh({
  book,
  position,
  index,
  onClick,
}: {
  book: Book;
  position: [number, number, number];
  index: number;
  onClick?: (book: Book) => void;
}) {
  const [hovered, setHovered] = useState(false)
  const meshRef = useRef<THREE.Mesh>(null)

  const width = 0.15 + Math.random() * 0.05
  const height = 0.8 + (book.title.length % 5) * 0.05
  const depth = 0.5

  const statusColor = getBookColor(book)
  const spineColor = BOOK_COLORS[index % BOOK_COLORS.length]

  return (
    <group position={position}>
      <mesh
        ref={meshRef}
        onPointerOver={(e) => {
          e.stopPropagation()
          setHovered(true)
          document.body.style.cursor = 'pointer'
        }}
        onPointerOut={() => {
          setHovered(false)
          document.body.style.cursor = 'default'
        }}
        onClick={(e) => {
          e.stopPropagation()
          onClick?.(book)
        }}
        scale={hovered ? [1.05, 1.05, 1.05] : [1, 1, 1]}
      >
        <boxGeometry args={[width, height, depth]} />
        <meshStandardMaterial
          color={hovered ? '#818cf8' : spineColor}
          roughness={0.6}
          metalness={0.1}
        />
      </mesh>

      {/* Status indicator strip on top */}
      <mesh position={[0, height / 2 + 0.02, 0]}>
        <boxGeometry args={[width, 0.03, depth]} />
        <meshStandardMaterial color={statusColor} emissive={statusColor} emissiveIntensity={0.3} />
      </mesh>

      {/* Tooltip on hover */}
      {hovered && (
        <Html
          position={[0, height / 2 + 0.3, 0]}
          center
          distanceFactor={6}
          style={{ pointerEvents: 'none' }}
        >
          <div className="bg-teal-900 text-white px-3 py-2 rounded-lg shadow-xl text-xs whitespace-nowrap">
            <div className="font-semibold">{book.title}</div>
            <div className="text-teal-200">{book.author}</div>
            <div className="mt-1 text-xs">
              <span
                className="inline-block w-2 h-2 rounded-full mr-1"
                style={{ backgroundColor: statusColor }}
              />
              {book.availableCopies}/{book.totalCopies} available
            </div>
          </div>
        </Html>
      )}
    </group>
  )
}

function ShelfBoard({ position, width }: { position: [number, number, number]; width: number }) {
  return (
    <RoundedBox args={[width, 0.06, 0.7]} position={position} radius={0.01} smoothness={4}>
      <meshStandardMaterial color="#92653b" roughness={0.8} metalness={0.05} />
    </RoundedBox>
  )
}

function ShelfSide({ position }: { position: [number, number, number] }) {
  return (
    <RoundedBox args={[0.08, 3.7, 0.7]} position={position} radius={0.01} smoothness={4}>
      <meshStandardMaterial color="#7a5430" roughness={0.8} metalness={0.05} />
    </RoundedBox>
  )
}

function Bookshelf({ books, onBookClick }: BookShelf3DProps) {
  const shelfWidth = 4.2
  const shelves = [1.5, 0.4, -0.7, -1.8] // y positions of shelf boards

  // Distribute books across shelves
  const booksPerShelf = Math.ceil(books.length / (shelves.length - 1))
  const shelfBooks: Book[][] = []
  for (let i = 0; i < shelves.length - 1; i++) {
    shelfBooks.push(books.slice(i * booksPerShelf, (i + 1) * booksPerShelf))
  }

  return (
    <group>
      {/* Shelf boards */}
      {shelves.map((y, i) => (
        <ShelfBoard key={i} position={[0, y, 0]} width={shelfWidth} />
      ))}

      {/* Shelf sides */}
      <ShelfSide position={[-shelfWidth / 2 - 0.04, -0.15, 0]} />
      <ShelfSide position={[shelfWidth / 2 + 0.04, -0.15, 0]} />

      {/* Back panel */}
      <mesh position={[0, -0.15, -0.35]}>
        <boxGeometry args={[shelfWidth + 0.16, 3.7, 0.04]} />
        <meshStandardMaterial color="#6b4423" roughness={0.9} />
      </mesh>

      {/* Books on shelves */}
      {shelfBooks.map((rowBooks, shelfIndex) => {
        const shelfY = shelves[shelfIndex]
        const startX = -shelfWidth / 2 + 0.3
        let currentX = startX

        return rowBooks.map((book, bookIndex) => {
          const bookWidth = 0.15 + Math.random() * 0.05
          const bookHeight = 0.8 + (book.title.length % 5) * 0.05
          const x = currentX
          currentX += bookWidth + 0.04
          const globalIndex = shelfIndex * booksPerShelf + bookIndex

          return (
            <BookMesh
              key={book.id}
              book={book}
              position={[x, shelfY + bookHeight / 2 + 0.04, 0]}
              index={globalIndex}
              onClick={onBookClick}
            />
          )
        })
      })}
    </group>
  )
}

export function BookShelf3D({ books, onBookClick }: BookShelf3DProps) {
  return (
    <div className="w-full h-[450px] bg-gradient-to-b from-stone-100 to-stone-200 rounded-xl overflow-hidden border">
      <Canvas
        camera={{ position: [0, 0, 5], fov: 50 }}
        shadows
      >
        <ambientLight intensity={0.5} />
        <directionalLight
          position={[5, 5, 5]}
          intensity={0.8}
          castShadow
          shadow-mapSize={[1024, 1024]}
        />
        <pointLight position={[-3, 3, 3]} intensity={0.4} color="#ffd699" />

        <Suspense fallback={null}>
          <Bookshelf books={books} onBookClick={onBookClick} />
        </Suspense>

        <OrbitControls
          enablePan={false}
          minDistance={3}
          maxDistance={10}
          minPolarAngle={Math.PI / 6}
          maxPolarAngle={Math.PI / 2}
        />
      </Canvas>

      {/* Legend */}
      <div className="flex justify-center gap-6 -mt-10 relative z-10 pb-3">
        <div className="flex items-center gap-1.5 text-xs text-stone-600 bg-white/80 backdrop-blur rounded px-2 py-1">
          <span className="w-2.5 h-2.5 rounded-full bg-green-500" />
          Available
        </div>
        <div className="flex items-center gap-1.5 text-xs text-stone-600 bg-white/80 backdrop-blur rounded px-2 py-1">
          <span className="w-2.5 h-2.5 rounded-full bg-yellow-500" />
          Low Stock
        </div>
        <div className="flex items-center gap-1.5 text-xs text-stone-600 bg-white/80 backdrop-blur rounded px-2 py-1">
          <span className="w-2.5 h-2.5 rounded-full bg-red-500" />
          Checked Out
        </div>
      </div>
    </div>
  )
}
