import { Component, OnDestroy, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators, AbstractControl, ValidationErrors, ValidatorFn } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { AuthService, UserService } from '../service';
import { Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';

interface DisplayMessage {
  msgType: string;
  msgBody: string;
}

@Component({
  selector: 'app-sign-up',
  templateUrl: './sign-up.component.html',
  styleUrls: ['./sign-up.component.css']
})
export class SignUpComponent implements OnInit, OnDestroy {

  title = 'Sign up';
  form!: FormGroup;
  submitted = false;
  notification: DisplayMessage | null = null;
  returnUrl!: string;
  private ngUnsubscribe: Subject<void> = new Subject<void>();

  constructor(
    private userService: UserService,
    private authService: AuthService,
    private router: Router,
    private route: ActivatedRoute,
    private formBuilder: FormBuilder
  ) {}

  ngOnInit() {
    this.route.params
      .pipe(takeUntil(this.ngUnsubscribe))
      .subscribe((params: any) => {
        this.notification = params as DisplayMessage;
      });

    // Set return URL or default to '/'
    this.returnUrl = this.route.snapshot.queryParams['returnUrl'] || '/';

    // Initialize form with validation
    this.form = this.formBuilder.group({
      username: ['', [
        Validators.required,
        Validators.minLength(3),
        Validators.maxLength(64)
      ]],
      password: ['', [
        Validators.required,
        Validators.minLength(3),
        Validators.maxLength(32)
      ]],
      confirmPassword: ['', [Validators.required]],
      firstname: [''],
      lastname: [''],
      email: ['', [
        Validators.required,
        this.gmailValidator
      ]],
      address: ['']
    },{ 
      validator: this.passwordsMatchValidator
    });

    this.form.controls['confirmPassword'].valueChanges.subscribe(() => {
      this.form.updateValueAndValidity();
    });
  }

  

  ngOnDestroy() {
    this.ngUnsubscribe.next();
    this.ngUnsubscribe.complete();
  }

  // Custom validator for Gmail format without special characters
  gmailValidator(control: AbstractControl): ValidationErrors | null {
    const emailPattern = /^[a-zA-Z0-9._%+-]+@gmail\.com$/;
    return emailPattern.test(control.value) ? null : { gmailFormat: true };
  }

  passwordsMatchValidator(formGroup: AbstractControl): ValidationErrors | null {
    const password = formGroup.get('password')?.value;
    const confirmPasswordControl = formGroup.get('confirmPassword');
    
    if (password !== confirmPasswordControl?.value) {
        confirmPasswordControl?.setErrors({ passwordMismatch: true });
        return { passwordMismatch: true };
    } else {
        confirmPasswordControl?.setErrors(null);
        return null;
    }
  }


  onSubmit() {
    this.notification = null; // Clear previous notification
    this.submitted = true;
  
    this.authService.signup(this.form.value).subscribe(
      data => {
        this.router.navigate(['/activate-account'], { queryParams: { email: this.form.value.email } });
      },
      error => {
        this.submitted = false;
        if (error.status === 400 && error.error) {
          // Display field-specific error message
          const field = error.error.field;
          const message = error.error.error;
          this.notification = { msgType: 'error', msgBody: `${field}: ${message}` };
        } else {
          // Handle other types of errors
          this.notification = { msgType: 'error', msgBody: 'An error occurred during signup. Please try again.' };
        }
      }
    );
  }
  
}
