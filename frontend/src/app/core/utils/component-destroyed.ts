import { Subject } from 'rxjs/Subject';

interface OnDestroyLike {
  ngOnDestroy(): void;
}

export function componentDestroyed(component: OnDestroyLike): Subject<{}> {
  const oldNgOnDestroy = component.ngOnDestroy;
  const stop$ = new Subject();
  component.ngOnDestroy = function () {
    oldNgOnDestroy && oldNgOnDestroy.apply(component);
    stop$.next(undefined);
    stop$.complete();
  };
  return stop$;
}
