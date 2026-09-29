import { Suspense } from "react";

/**
 * Wrapper para componentes que usan useSearchParams()
 * Next.js 15+ requiere envolver estos componentes en Suspense
 */
export function withSuspense(Component, fallback = <div>Cargando...</div>) {
  return function WrappedComponent(props) {
    return (
      <Suspense fallback={fallback}>
        <Component {...props} />
      </Suspense>
    );
  };
}
