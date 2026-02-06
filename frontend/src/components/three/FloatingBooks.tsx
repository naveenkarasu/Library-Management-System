import { useRef, useMemo } from 'react'
import { Canvas, useFrame } from '@react-three/fiber'
import * as THREE from 'three'

function Book({ position, rotation, speed, color }: { position: [number, number, number]; rotation: [number, number, number]; speed: number; color: string }) {
  const meshRef = useRef<THREE.Mesh>(null)
  const initialY = position[1]

  useFrame((state) => {
    if (meshRef.current) {
      meshRef.current.rotation.x += 0.002 * speed
      meshRef.current.rotation.y += 0.003 * speed
      meshRef.current.position.y = initialY + Math.sin(state.clock.elapsedTime * speed * 0.5) * 0.4
    }
  })

  return (
    <mesh ref={meshRef} position={position} rotation={rotation}>
      <boxGeometry args={[0.8, 1.1, 0.15]} />
      <meshStandardMaterial color={color} roughness={0.6} metalness={0.1} />
    </mesh>
  )
}

function Books() {
  const books = useMemo(() => {
    const tealGoldPalette = ['#0d9488', '#14b8a6', '#2dd4bf', '#d97706', '#f59e0b', '#fbbf24', '#0f766e', '#115e59', '#b45309', '#92400e']
    return Array.from({ length: 20 }, (_, i) => ({
      position: [
        (Math.random() - 0.5) * 16,
        (Math.random() - 0.5) * 10,
        (Math.random() - 0.5) * 8 - 2,
      ] as [number, number, number],
      rotation: [
        Math.random() * Math.PI,
        Math.random() * Math.PI,
        Math.random() * Math.PI,
      ] as [number, number, number],
      speed: 0.3 + Math.random() * 0.7,
      color: tealGoldPalette[i % tealGoldPalette.length],
    }))
  }, [])

  return (
    <>
      {books.map((book, i) => (
        <Book key={i} {...book} />
      ))}
    </>
  )
}

export function FloatingBooks() {
  return (
    <div className="absolute inset-0 -z-10">
      <Canvas camera={{ position: [0, 0, 6], fov: 60 }} style={{ background: 'transparent' }}>
        <ambientLight intensity={0.4} />
        <directionalLight position={[5, 5, 5]} intensity={0.3} />
        <pointLight position={[-3, 2, 4]} intensity={0.2} color="#0d9488" />
        <pointLight position={[3, -2, 4]} intensity={0.2} color="#d97706" />
        <fog attach="fog" args={['#fafaf9', 5, 15]} />
        <Books />
      </Canvas>
    </div>
  )
}
