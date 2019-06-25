from django.conf.urls import patterns, include, url

# Uncomment the next two lines to enable the admin:
# from django.contrib import admin
# admin.autodiscover()
from control.views import auto_mode,manual_mode,light,turn_light_on,turn_light_off,turn_ac_on,turn_ac_off,change_mode
urlpatterns = patterns[
    # Examples:
    # url(r'^$', 'IOT.views.home', name='home'),
    # url(r'^IOT/', include('IOT.foo.urls')),

    # Uncomment the admin/doc line below to enable admin documentation:
    # url(r'^admin/doc/', include('django.contrib.admindocs.urls')),

    # Uncomment the next line to enable the admin:
    # url(r'^admin/', include(admin.site.urls)),
  url(r'^auto/$',auto_mode),
  url(r'^manual/$',manual_mode),
  url(r'^light/$',light),
	url(r'^lo/$',turn_light_on),
	url(r'^loff/$',turn_light_off),
	url(r'^ao/$',turn_ac_on),
	url(r'^aoff/$',turn_ac_off),
	url(r'^cm/$',change_mode),
]
