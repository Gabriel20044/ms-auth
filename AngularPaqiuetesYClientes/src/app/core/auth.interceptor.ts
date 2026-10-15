import { HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { AuthService } from './auth.service';
import { environment } from './environment';

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const auth = inject(AuthService);
  const token = auth.getToken();

  const esLogin = req.url.startsWith(`${environment.apiUrl}/login`);

  if (token && !esLogin) {
    const clon = req.clone({
      setHeaders: { Authorization: `Bearer ${token}` }
    });
    return next(clon);
  }
  return next(req);
};
