import { HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { MessageService } from 'primeng/api';
import { catchError, throwError } from 'rxjs';

export const httpErrorInterceptor: HttpInterceptorFn = (req, next) => {
  const messageService = inject(MessageService);

  return next(req).pipe(
    catchError(error =>{
      if(error.status != 200){
          messageService.add({key:'messageToast' ,severity: 'error',summary:'Error', detail: `${error.error.message}` })
      }
      return throwError(error)
    })
  );
};
